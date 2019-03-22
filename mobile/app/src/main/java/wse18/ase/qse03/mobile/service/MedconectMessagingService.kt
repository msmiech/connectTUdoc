package wse18.ase.qse03.mobile.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.provider.Settings.Global.getString
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import wse18.ase.qse03.mobile.R
import wse18.ase.qse03.mobile.ui.MainActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager


open class MedconectMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        Log.d(TAG, "From: ${remoteMessage?.from}")

        // Check if message contains a data payload.
        remoteMessage?.data?.isNotEmpty()?.let {
            Log.wtf(TAG, "Message data payload: " + remoteMessage.data)
            val officeId: Long =
                if (!remoteMessage.data["data"].isNullOrEmpty()) remoteMessage.data["data"]!!.toLong() else -1

            // Notify chat fragment that new chat messages are available
            val broadcaster = LocalBroadcastManager.getInstance(baseContext)
            val intent = Intent(RELOAD_MESSAGES)
            intent.putExtra("officeId", officeId)
            broadcaster.sendBroadcast(intent)

            if (!remoteMessage.data["notificationTitle"].isNullOrEmpty() || !remoteMessage.data["notificationBody"].isNullOrEmpty()) {
                sendNotification(
                    remoteMessage.data["notificationTitle"]!!,
                    remoteMessage.data["notificationBody"]!!,
                    officeId
                )
            }
        }

        // Check if message contains a notification payload.
        /*remoteMessage?.notification?.let {
            val officeId: Long = if (remoteMessage.data["data"] != null) remoteMessage.data["data"]!!.toLong() else -1
            sendNotification(it.title ?: "",it.body ?: "", officeId)
        }*/
    }

    override fun onNewToken(token: String?) {
        Log.d(TAG, "Refreshed token: $token")

        val sharedPref =
            getSharedPreferences(getString(R.string.fcm_registration_token), Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putString(getString(R.string.fcm_registration_token), token)
            commit()
        }
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private fun sendNotification(messageTitle: String, messageBody: String, officeId: Long) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("officeId", officeId)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, 0 /* Request code */, intent,
            PendingIntent.FLAG_ONE_SHOT
        )


        val channelId = getString(R.string.default_notification_channel_id)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        // Since Android Oreo notification channels are required
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "MedConnect Benachrichtigungen",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.icon)
            .setContentTitle(messageTitle)
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)


        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }

    companion object {

        const val RELOAD_MESSAGES = "RELOAD_MESSAGES"
        private const val TAG = "MessagingService"
    }
}