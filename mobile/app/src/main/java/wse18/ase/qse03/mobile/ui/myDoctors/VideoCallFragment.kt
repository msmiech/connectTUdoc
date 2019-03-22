package wse18.ase.qse03.mobile.ui.myDoctors

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.media.AudioManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import org.webrtc.*
import wse18.ase.qse03.mobile.R
import wse18.ase.qse03.mobile.ui.MainActivityViewModel
import wse18.ase.qse03.mobile.util.FirebaseUtil


class VideoCallFragment : Fragment() {
    private lateinit var viewModel: MainActivityViewModel
    private var signaler: SignalingFirebase? = null
    private var videoSession: VideoCallSession? = null
    private var localVideoView: SurfaceViewRenderer? = null
    private var remoteVideoView: SurfaceViewRenderer? = null
    private var audioManager: AudioManager? = null
    private var savedMicrophoneState: Boolean? = null
    private var savedAudioMode: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = activity?.run {
            ViewModelProviders.of(this).get(MainActivityViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
        this.signaler = viewModel.getSignaler()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        FirebaseUtil.getFirebaseUser() ?: return inflater.inflate(R.layout.video_call, container, false)
        val view: View = inflater.inflate(R.layout.video_call, container, false)
        localVideoView = view.findViewById(R.id.my_video)
        remoteVideoView = view.findViewById(R.id.remote_video)

        val hangup: ImageButton = view.findViewById(R.id.hangup_button)
        hangup.setOnClickListener {
            videoSession?.terminate()
            localVideoView?.release()
            remoteVideoView?.release()

            if (savedAudioMode !== null) {
                audioManager?.mode = savedAudioMode!!
            }
            if (savedMicrophoneState != null) {
                audioManager?.isMicrophoneMute = savedMicrophoneState!!
            }
            Navigation.findNavController(it!!).navigate(R.id.myDoctors_dest, Bundle())
        }

        // audioManager = this.getSystemService(Context.AUDIO_SERVICE) as AudioManager?
        savedAudioMode = audioManager?.mode
        audioManager?.mode = AudioManager.MODE_IN_COMMUNICATION

        savedMicrophoneState = audioManager?.isMicrophoneMute
        audioManager?.isMicrophoneMute = false

        val builder = AlertDialog.Builder(context)
        builder.setTitle(R.string.msg_call)
        builder.setPositiveButton(R.string.btn_accept) { dialog, id ->
            handlePermissions()
        }
        builder.setNegativeButton(R.string.btn_decline)
        { dialog, id ->
            this.signaler?.cleanUpCall()
            onDestroy()
            Navigation.findNavController(view!!).navigate(R.id.doctorSearch_dest)
            // User cancelled the dialog
        }
        builder.create().show()
        return view
    }

    override fun onDestroy() {
        super.onDestroy()

        videoSession?.terminate()
        localVideoView?.release()
        remoteVideoView?.release()
        if (savedAudioMode !== null) {
            audioManager?.mode = savedAudioMode!!
        }
        if (savedMicrophoneState != null) {
            audioManager?.isMicrophoneMute = savedMicrophoneState!!
        }
    }


    private fun handlePermissions() {
        val canAccessCamera = ContextCompat.checkSelfPermission(
            this.context!!,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
        val canRecordAudio = ContextCompat.checkSelfPermission(
            this.context!!,
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED
        if (!canAccessCamera || !canRecordAudio) {
            Toast.makeText(this.context!!, "Keine Berechtigung für Kamera und Mikrofon gewährt!", Toast.LENGTH_LONG)
                .show()
            requestPermissions(
                arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO),
                CAMERA_AUDIO_PERMISSION_REQUEST
            )
        } else {
            startVideoSession()
        }
    }

    private fun startVideoSession() {
        videoSession =
                VideoCallSession.connect(this.context!!, signaler!!, VideoRenderers(localVideoView, remoteVideoView))
        remoteVideoView?.init(videoSession?.renderContext, null)
        remoteVideoView?.setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_FILL)
        remoteVideoView?.setEnableHardwareScaler(true)
        localVideoView?.init(videoSession?.renderContext, null)
        localVideoView?.setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_FIT)
        localVideoView?.setEnableHardwareScaler(true)
        localVideoView?.bringToFront()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        Log.w(TAG, "onRequestPermissionsResult: $requestCode $permissions $grantResults")
        when (requestCode) {
            CAMERA_AUDIO_PERMISSION_REQUEST -> {
                if (grantResults.isNotEmpty() && grantResults.first() == PackageManager.PERMISSION_GRANTED) {
                    startVideoSession()
                } else {
                    //  finish()
                }
                return
            }
        }
    }

    companion object {
        private val CAMERA_AUDIO_PERMISSION_REQUEST = 1
        private val TAG = "VideoCallActivity"
        private val BACKEND_URL = "ws://HOST:8000/" // Change HOST to your server's IP if you want to test
    }
}