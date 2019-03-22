package wse18.ase.qse03.mobile.ui.myDoctors

import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.Toast
import com.google.firebase.database.*
import org.webrtc.*
import java.util.concurrent.Executors

import wse18.ase.qse03.mobile.model.ClientMessage
import wse18.ase.qse03.mobile.model.ICEMessage
import wse18.ase.qse03.mobile.model.SDPMessage
import java.util.concurrent.ArrayBlockingQueue
import android.os.AsyncTask



data class VideoRenderers(private var localView: SurfaceViewRenderer?, private var remoteView: SurfaceViewRenderer?) {
    val localRenderer: (VideoRenderer.I420Frame) -> Unit = { f ->
        localView?.renderFrame(f) ?: sink(f)
    }
    val remoteRenderer: (VideoRenderer.I420Frame) -> Unit = { f ->
        remoteView?.renderFrame(f) ?: sink(f)
    }

    fun updateViewRenders(localView: SurfaceViewRenderer, remoteView: SurfaceViewRenderer) {
        this.localView = localView
        this.remoteView = remoteView
    }

    private fun sink(frame: VideoRenderer.I420Frame) {
        Log.w("VideoRenderer", "Missing surface view, dropping frame")
        VideoRenderer.renderFrameDone(frame)
    }
}

class VideoCallSession(
    private val context: Context,
    private val signaler: SignalingFirebase,
    private val videoRenderers: VideoRenderers)  {

    private var peerConnection : PeerConnection? = null
    private var factory : PeerConnectionFactory? = null
    private var mediaStream: MediaStream? = null
    private var videoSource : VideoSource? = null
    private var audioSource : AudioSource? = null
    private val eglBase = EglBase.create()
    private var videoCapturer: VideoCapturer? = null
    private var videoTrack: VideoTrack? = null

    private val videoHeight = 1280
    private val videoWidth = 720
    private val videoFPS = 30
    private var readMessages = false
    private val TAG ="VideoRenderer"
    val renderContext: EglBase.Context
        get() = eglBase.eglBaseContext

    class SimpleRTCEventHandler (
        private val onIceCandidateCb: (IceCandidate) -> Unit,
        private val onAddStreamCb: (MediaStream) -> Unit,
        private val onRemoveStreamCb: (MediaStream) -> Unit) : PeerConnection.Observer {

        override fun onIceCandidate(candidate: IceCandidate?) {
            Log.e(TAG, "handle local ice candidate")
            if(candidate != null) onIceCandidateCb(candidate)
        }

        override fun onAddStream(stream: MediaStream?) {
            if (stream != null) onAddStreamCb(stream)
        }

        override fun onRemoveStream(stream: MediaStream?) {
            if(stream != null) onRemoveStreamCb(stream)
        }

        override fun onDataChannel(chan: DataChannel?) { Log.w(TAG, "onDataChannel: $chan") }

        override fun onIceConnectionReceivingChange(p0: Boolean) { Log.w(TAG, "onIceConnectionReceivingChange: $p0") }

        override fun onIceConnectionChange(newState: PeerConnection.IceConnectionState?) { Log.w(TAG, "onIceConnectionChange: $newState") }

        override fun onIceGatheringChange(newState: PeerConnection.IceGatheringState?) { Log.w(TAG, "onIceGatheringChange: $newState") }

        override fun onSignalingChange(newState: PeerConnection.SignalingState?) { Log.w(TAG, "onSignalingChange: $newState") }

        override fun onIceCandidatesRemoved(candidates: Array<out IceCandidate>?) { Log.w(TAG, "onIceCandidatesRemoved: $candidates") }

        override fun onRenegotiationNeeded() { Log.w(TAG, "onRenegotiationNeeded") }

        override fun onAddTrack(receiver: RtpReceiver?, streams: Array<out MediaStream>?) { }
    }

    init {
       // signaler.messageHandler = this::onMessage
        executor.execute(this::init)
        readMessages = true
    }

    private fun init() {
        PeerConnectionFactory.initializeAndroidGlobals(context, true)
        val opts = PeerConnectionFactory.Options()
        opts.networkIgnoreMask = 0

        factory = PeerConnectionFactory(opts)
        factory?.setVideoHwAccelerationOptions(eglBase.eglBaseContext, eglBase.eglBaseContext)

        val iceServers = arrayListOf(
            PeerConnection.IceServer.builder("stun:stun.l.google.com:19302").createIceServer()
        )

        val constraints = MediaConstraints()
        constraints.mandatory.add(MediaConstraints.KeyValuePair("OfferToReceiveAudio", "true"))
        constraints.mandatory.add(MediaConstraints.KeyValuePair("OfferToReceiveVideo", "true"))
        val rtcCfg = PeerConnection.RTCConfiguration(iceServers)
        rtcCfg.continualGatheringPolicy = PeerConnection.ContinualGatheringPolicy.GATHER_CONTINUALLY
        val rtcEvents = SimpleRTCEventHandler(this::handleLocalIceCandidate, this::addRemoteStream, this::removeRemoteStream)
        peerConnection = factory?.createPeerConnection(rtcCfg, constraints, rtcEvents)
        setupMediaDevices()
        AsyncTask.execute {
            var queue: ArrayBlockingQueue<ClientMessage> =  this.signaler.getMessageQueue()
            while(readMessages) {
                Log.e("Test Async","new message")
                this.onMessage(queue.take())
            }
        }
    }

   /* private fun start() {
       signaler.init()
    } */


    private fun addRemoteStream(stream: MediaStream) {
        Log.i(TAG, "Got remote stream: $stream")
        executor.execute {
            if(stream.videoTracks.isNotEmpty()) {
                Log.i(TAG, "Is not empty")
                val remoteVideoTrack = stream.videoTracks.first()
                remoteVideoTrack.setEnabled(true)
                remoteVideoTrack.addRenderer(VideoRenderer(videoRenderers.remoteRenderer))
            } else {
                Log.i(TAG, "Is empty")
            }
        }
    }

    private fun removeRemoteStream(@Suppress("UNUSED_PARAMETER") _stream: MediaStream) {
        // We lost the stream, lets finish
        Log.i(TAG, "Bye")
    }

    private fun handleRemoteCandidate(label: Int, id: String, strCandidate: String) {
        Log.i(TAG, "Got remote ICE candidate $strCandidate")
        executor.execute {
            val candidate = IceCandidate(id, label, strCandidate)
            if(peerConnection == null) {
                Log.i(TAG, "Peer connection is null")
            }
            peerConnection?.addIceCandidate(candidate)
        }
    }

    private fun handleLocalIceCandidate(candidate: IceCandidate) {
        Log.w(TAG, "Local ICE candidate: $candidate")
        signaler.sendCandidate(candidate.sdpMLineIndex, candidate.sdpMid, candidate.sdp)
    }

    private fun setupMediaDevices() {
        mediaStream = factory?.createLocalMediaStream(STREAM_LABEL)
        mediaStream?.addTrack(setupVideoTrack(isFront))
        audioSource = factory?.createAudioSource(createAudioConstraints())
        val audioTrack = factory?.createAudioTrack(AUDIO_TRACK_LABEL, audioSource)
        mediaStream?.addTrack(audioTrack)
        peerConnection?.addStream(mediaStream)
    }

    private fun createAudioConstraints(): MediaConstraints {
        val audioConstraints = MediaConstraints()
        audioConstraints.mandatory.add(MediaConstraints.KeyValuePair("googEchoCancellation", "true"))
        audioConstraints.mandatory.add(MediaConstraints.KeyValuePair("googAutoGainControl", "false"))
        audioConstraints.mandatory.add(MediaConstraints.KeyValuePair("googHighpassFilter", "false"))
        audioConstraints.mandatory.add(MediaConstraints.KeyValuePair("googNoiseSuppression", "true"))
        return audioConstraints
    }

    private fun setupVideoTrack(front: Boolean): VideoTrack? {
        val camera = if (useCamera2()) Camera2Enumerator(context) else Camera1Enumerator(false)

        videoCapturer = if (front) createFrontCameraCapturer(camera) else createBackCameraCapturer(camera)
        val videoSource = factory?.createVideoSource(videoCapturer)
        videoCapturer?.startCapture(videoHeight, videoWidth, videoFPS)
        val videoRenderer = VideoRenderer(videoRenderers.localRenderer)

        videoTrack = factory?.createVideoTrack(VIDEO_TRACK_LABEL, videoSource)
        videoTrack?.addRenderer(videoRenderer)
        return videoTrack
    }


    private fun handleRemoteDescriptor(sdp: String) {
        peerConnection?.setRemoteDescription(SDPSetCallback({ setError ->
        if(setError != null) {
            Log.e(TAG, "setRemoteDescription failed: $setError")
         } else {
            Log.e(TAG, "setRemoteDescription did not fail")
             peerConnection?.createAnswer(SDPCreateCallback(this::createDescriptorCallback), MediaConstraints())
         }
         }), SessionDescription(SessionDescription.Type.OFFER, sdp))
    }

    private fun createDescriptorCallback(result: SDPCreateResult) {
        when(result) {
            is SDPCreateSuccess -> {
                peerConnection?.setLocalDescription(SDPSetCallback({ setResult ->
                    Log.i(TAG, "SetLocalDescription: $setResult")
                }), result.descriptor)
                signaler.sendSDP("answer",result.descriptor.description)
            }
            is SDPCreateFailure -> Log.e(TAG, "Error creating offer: ${result.reason}")
        }
    }


    private fun onMessage(message: ClientMessage) {
        when(message) {
            is SDPMessage -> {
                handleRemoteDescriptor(message.sdp)
            }
            is ICEMessage -> {
                handleRemoteCandidate(message.sdpMLineIndex, message.sdpMid, message.candidate)
            }
        }
    }



    fun terminate() {
        readMessages = false
        //signaler.close()
        this.signaler.cleanUpCall()
        try {
            videoCapturer?.stopCapture()
        } catch (ex: Exception) { }
        videoCapturer?.dispose()
        videoSource?.dispose()
        audioSource?.dispose()
        peerConnection?.dispose()
        factory?.dispose()
        eglBase.release()
    }

    private var isFront = true

    fun toggleCamera() {
        isFront = !isFront
        mediaStream?.removeTrack(videoTrack)
        videoTrack?.dispose()
        mediaStream?.addTrack(setupVideoTrack(isFront))
    }

    private fun createFrontCameraCapturer(enumerator: CameraEnumerator): VideoCapturer? {
        val deviceNames = enumerator.deviceNames
        //find the front facing camera and return it.
        deviceNames
            .filter { enumerator.isFrontFacing(it) }
            .mapNotNull { enumerator.createCapturer(it, null) }
            .forEach { return it }

        return null
    }

    private fun createBackCameraCapturer(enumerator: CameraEnumerator): VideoCapturer? {
        // Front facing camera not found, try something else
        Logging.d(TAG, "Looking for other cameras.")
        val deviceNames = enumerator.deviceNames
        //find the front facing camera and return it.
        deviceNames
            .filter { enumerator.isBackFacing(it) }
            .mapNotNull {
                Logging.d(TAG, "Creating other camera capturer.")
                enumerator.createCapturer(it, null)
            }
            .forEach { return it }

        Toast.makeText(context, "No back camera found!", Toast.LENGTH_SHORT).show()
        return createFrontCameraCapturer(enumerator)
    }

    private fun useCamera2(): Boolean {
        return Camera2Enumerator.isSupported(context)
    }

    companion object {

        fun connect(context: Context, signaler: SignalingFirebase, videoRenderers: VideoRenderers): VideoCallSession {
            val firebaseHandler = signaler
            return VideoCallSession(context, firebaseHandler, videoRenderers)
        }

        private const val STREAM_LABEL = "remoteStream"
        private const val VIDEO_TRACK_LABEL = "remoteVideoTrack"
        private const val AUDIO_TRACK_LABEL = "remoteAudioTrack"
        private const val TAG = "VideoCallSession"
        private val executor = Executors.newSingleThreadExecutor()
        private val messageExecutor = Executors.newSingleThreadExecutor()
    }
}