package pictale.mk.service

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import android.util.Log.d
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import pictale.mk.DetailsActivity
import pictale.mk.HomeActivity
import pictale.mk.R


//const val  channelId="NotificationChannel"
//const val  channelName="NameChannel"


class FirebaseMService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        Log.d("TAG", "Refreshed token: $token")
//        sendRegistrationToServer(token)
    }




    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "From: ${remoteMessage.from}")

        if (remoteMessage.data.isNotEmpty()) {
            Log.d(TAG, "Message data payload: ${remoteMessage.data}")

            d("data--","${remoteMessage.data["type"]}")
            when(remoteMessage.data["type"]) {
                "userRequestAccess" -> handleUserJoin(remoteMessage)
//                "uploadFile" -> handleNewPhoto(remoteMessage)
//                "newUser" -> handleNewUser(remoteMessage)
                else -> handleUnknownMessage(remoteMessage)
            }
        }

        // Check if message contains a notification payload.
        if (remoteMessage.notification != null) {
            Log.d(TAG, "Message Notification Body: ${remoteMessage.notification!!.body}")

            // Handle notification payload
            handleNotificationPayload(remoteMessage.notification!!)
        }
    }

    private fun handleUnknownMessage(remoteMessage: RemoteMessage) {
            Log.w(TAG, "Received an unknown message: ${remoteMessage.data}")
            val message = getString(R.string.default_message)
            sendNotification(message)
    }

    private fun sendNotification(message: String) {
        val intent = Intent(this, DetailsActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)

        val channelId = getString(R.string.default_notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        d("channelId","$channelId")
        d("defaultSoundUri","$defaultSoundUri")
        d("pendingIntent","$pendingIntent")

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_account)
            .setContentTitle(getString(R.string.app_name))
            .setContentText(message)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        d("notificationBuilder","$notificationBuilder")


        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        d("notificationManager","$notificationManager")

        // Since Android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Channel human readable title", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0, notificationBuilder.build())
    }

    @SuppressLint("StringFormatInvalid")
    private fun handleUserJoin(remoteMessage: RemoteMessage) {
        val data = remoteMessage.data

        val username = data["username"]

        val message = getString(R.string.user_join_message, username)

        sendNotification(message)
    }

    private fun handleNotificationPayload(notification: RemoteMessage.Notification) {
        val title = notification.title
        val message = notification.body
        val imageUrl = notification.imageUrl

        if (imageUrl != null) {

        }

        sendNotification(title, message)
    }

    private fun sendNotification(title: String?, message: String?) {
        val intent = Intent(this, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_MUTABLE)
        val CHANNEL_ID ="Notification"
        // Create a notification builder with the required fields
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_account)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        // Show the notification
        val notificationId = System.currentTimeMillis().toInt()

        with(NotificationManagerCompat.from(this)) {
            notify(notificationId, builder.build())
        }
    }
}