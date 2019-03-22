package wse18.ase.qse03.mobile.ui.myDoctors

import android.os.Bundle
import android.util.Log
import androidx.navigation.Navigation
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.firebase.database.*
import org.json.JSONObject
import org.webrtc.*
import wse18.ase.qse03.mobile.R
import wse18.ase.qse03.mobile.model.ClientMessage
import wse18.ase.qse03.mobile.model.ICEMessage
import wse18.ase.qse03.mobile.model.MessageType
import wse18.ase.qse03.mobile.model.SDPMessage
import java.io.Serializable
import java.util.*
import java.util.concurrent.ArrayBlockingQueue

class SignalingFirebase(
    private var databaseReference: DatabaseReference
) {
    private val TAG = "FirebaseSignaler"
    private var receivedOffer = false
    private var senderId: String = ""
    private var currentCaller: String? = null
    private var messageList: ArrayBlockingQueue<ClientMessage> = ArrayBlockingQueue(1000)

    var messageHandler: ((ClientMessage) -> Unit)? = null
    private var onCallReReceivedListener: OnCallReceivedListener? = null

    private val dataListener: ChildEventListener = object : ChildEventListener, Serializable {

        fun onMessage(dataSnapshot: DataSnapshot) {
            Log.i(TAG,"onMessage")
            var clientMessage: ClientMessage? = null

            if (!dataSnapshot.exists()) return

            if(!(dataSnapshot.value is ArrayList<*>)) return

            val snapshotData  = dataSnapshot.value as ArrayList<String>

            if(snapshotData.size != 1) return

            val map = snapshotData.get(0) as Map<String,String>
            if(!map.containsKey("message") || !map.containsKey("sender")) {
                return
            }
            Log.e("Important Message", map.get("message"))
            var sender = map.get("sender")
            if(sender != null && sender == senderId) return

            if(currentCaller != null && currentCaller != sender) {
                return
            }
            var message = JSONObject(map.get("message"))
            val m = ObjectMapper()

            if(message.has("ice")) {
                if(receivedOffer) {
                    clientMessage = m.readValue(message.getString(MessageType.ICEMessage.value), ICEMessage::class.java)
                }
            } else if (message.has("sdp")) {
                if(!receivedOffer) {
                    clientMessage = m.readValue(message.getString(MessageType.SDPMessage.value), SDPMessage::class.java)
                    receivedOffer = true
                    currentCaller = sender
                }
            } else {
                clientMessage = null
            }

            if(receivedOffer && clientMessage != null && clientMessage.kind == MessageType.SDPMessage) {
                //aks if call should be taken or not
                //switch view for handling
                onCallReReceivedListener?.onCallReceived()
                Log.i(TAG, "FirebaseSignaler: Decoded message as ${clientMessage?.kind}")
            }

            if (clientMessage != null)  {
                Log.i(TAG,"Message was accepted")
                messageList.add(clientMessage)
                //messageHandler!!.invoke(clientMessage)
            }
        }
        override fun onChildAdded(dataSnapshot: DataSnapshot, p1: String?) {
            Log.i(TAG, "onChildAdded")
            onMessage(dataSnapshot)
        }

        override fun onChildChanged(dataSnapshot: DataSnapshot, p1: String?) {
            Log.i(TAG, "onChildChanged")
            onMessage(dataSnapshot)
        }

        override fun onChildMoved(dataSnapshot: DataSnapshot, p1: String?) {
            Log.i(TAG, "onChildMoved")
        }

        override fun onChildRemoved(dataSnapshot: DataSnapshot) {
            Log.i(TAG, "onChildRemoved")
        }

        override fun onCancelled(e: DatabaseError) {
            Log.i(TAG, "databaseError:", e.toException())
        }
    }

    interface OnCallReceivedListener {
        fun onCallReceived()
    }

    fun getMessageQueue(): ArrayBlockingQueue<ClientMessage> {
        return messageList
    }


    fun setOnCallReceivedListener(receivedListener: OnCallReceivedListener) {
        this.onCallReReceivedListener = receivedListener
    }

    fun listen(senderId: String) {
        Log.e("MainActivityViewModel","Called listen on signaler")
        this.senderId = senderId
        this.databaseReference.addChildEventListener(dataListener)
    }

    private fun send(clientMessage: ClientMessage) {
        var value: ArrayList<HashMap<String,Any>> = ArrayList<HashMap<String,Any>>()
        var map: HashMap<String,Any> = HashMap<String,Any>()
        map.put("sender",this.senderId)

        var messageMap: JSONObject = JSONObject()

        if(clientMessage.kind.value == "ice") {
            val ice: ICEMessage = clientMessage as ICEMessage
            messageMap.put("candidate",ice.candidate)
            messageMap.put("sdpMid",ice.sdpMid)
            messageMap.put("sdpMLineIndex",ice.sdpMLineIndex)
        } else {
            val sdp: SDPMessage = clientMessage as SDPMessage
            messageMap.put("type",sdp.type)
            messageMap.put("sdp",sdp.sdp)
        }
        var type: JSONObject = JSONObject()
        type.put(clientMessage.kind.value, messageMap)
        map.put("message",type.toString())
        Log.i(TAG, "Sended message " + type.toString())
        value.add(map)

        if(this.databaseReference == null) return
        this.databaseReference.child(System.currentTimeMillis().toString()).setValue((value)).addOnCompleteListener {
            if (it.isSuccessful) {
                Log.i(TAG, "FirebaseSignaler: Sended succesfully ${clientMessage.kind.value}")
                this.databaseReference.removeValue()
            } else
                Log.w(TAG, "Message of type '${clientMessage.kind.value}' can't be sent to the server")
        }
        this.databaseReference.push()
    }

    fun cleanUpCall() {
        this.receivedOffer = false
        this.currentCaller = null
        this.messageList = ArrayBlockingQueue<ClientMessage>(1000)
    }

    fun close() {
        Log.w(TAG, "close")
        this.databaseReference?.removeEventListener(dataListener)
    }

    fun sendSDP(type: String,sdp: String) {
        Log.w(TAG, "sendSDP : " + type + " " + sdp)
        send(SDPMessage(type,sdp))
    }

    fun sendCandidate(sdpMLineIndex: Int, sdpMid: String, candidate: String) {
        Log.w(TAG, "sendCandidate :  $sdpMLineIndex $sdpMid $candidate")
        send(ICEMessage(sdpMLineIndex, sdpMid, candidate))
    }
}