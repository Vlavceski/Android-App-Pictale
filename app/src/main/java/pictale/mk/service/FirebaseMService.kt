package pictale.mk.service

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.util.Log.d
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import pictale.mk.HomeActivity
import pictale.mk.R


//const val  channelId="NotificationChannel"
//const val  channelName="NameChannel"


class FirebaseMService : FirebaseMessagingService() {
    lateinit var notificationManager: NotificationManager
    lateinit var notificationChannel: NotificationChannel
    lateinit var builder: Notification.Builder

    override fun onNewToken(token: String) {
        Log.d("TAG", "Refreshed token: $token")
//        sendRegistrationToServer(token)
    }




    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "From: ${remoteMessage.from}")
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (remoteMessage.data.isNotEmpty()) {
            Log.d(TAG, "Message data payload: ${remoteMessage.data}")

            d("data--","${remoteMessage.data["type"]}")
            when(remoteMessage.data["type"]) {
                "userRequestAccess" -> handleUserJoin(remoteMessage.notification?.title,remoteMessage.notification?.body!!)
//                "uploadFile" -> handleNewPhoto(remoteMessage)
//                "newUser" -> handleNewUser(remoteMessage)
                else -> handleUnknownMessage(remoteMessage)
            }
        }

        // Check if message contains a notification payload.
        if (remoteMessage.notification != null) {
            Log.d(TAG, "Message Notification Body: ${remoteMessage.notification!!.body}")

            // Handle notification payload
//            handleNotificationPayload(remoteMessage.notification!!)
        }
    }

    private fun handleUnknownMessage(remoteMessage: RemoteMessage) {
            Log.w(TAG, "Received an unknown message: ${remoteMessage.data}")
            val message = getString(R.string.default_message)
//            sendNotification(message)
//        if (remoteMessage.notification != null) {
//            showNotification(remoteMessage.notification?.title, remoteMessage.notification?.body)
//        } else {
//            showNotification(remoteMessage.data["title"], remoteMessage.data["message"])
//        }
//        showNotifications(remoteMessage)
    }

    private fun showNotifications(title:String,remoteMessage: String?) {

        val intent = Intent(this, HomeActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val contentView = RemoteViews(packageName, R.layout.notification)
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = NotificationChannel(title, remoteMessage, NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.GREEN
            notificationChannel.enableVibration(false)
            notificationManager.createNotificationChannel(notificationChannel)

            builder = Notification.Builder(this, title)
                .setContent(contentView)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setLargeIcon(BitmapFactory.decodeResource(this.resources, R.drawable.ic_launcher_background))
                .setContentIntent(pendingIntent)
        } else {

            builder = Notification.Builder(this)
                .setContent(contentView)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setLargeIcon(BitmapFactory.decodeResource(this.resources, R.drawable.ic_launcher_background))
                .setContentIntent(pendingIntent)
        }
        notificationManager.notify(1234, builder.build())
    }


//    @SuppressLint("UnspecifiedImmutableFlag")
//    private fun showNotification(
//        title: String?,
//        body: String?
//    ) {
//        val intent = Intent()
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//        val pendingIntent = PendingIntent.getActivity(
//            this, 0, intent,
//            PendingIntent.FLAG_ONE_SHOT
//        )
//
//        val channelId = getString(R.string.channel_id)
//        val channelName = getString(R.string.channel_name)
//        val notificationManager =
//            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//
//        // Since android Oreo notification channel is needed.
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            setupNotificationChannels(channelId, channelName, notificationManager)
//        }
//
//        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
//
//        val notificationBuilder = NotificationCompat.Builder(this, channelId)
//            .setSmallIcon(R.mipmap.ic_launcher)
//            .setContentTitle(title)
//            .setContentText(body)
//            .setAutoCancel(true)
//            .setSound(soundUri)
//            .setContentIntent(pendingIntent)
//
//        notificationManager.notify(0, notificationBuilder.build())
//    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun setupNotificationChannels(
        channelId: String,
        channelName: String,
        notificationManager: NotificationManager
    ) {

        val channel =
            NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_LOW)
        channel.enableLights(true)
        channel.lightColor = Color.GREEN
        channel.enableVibration(true)
        notificationManager.createNotificationChannel(channel)
    }


//    private fun sendNotification(message: String) {
//        val intent = Intent(this, DetailsActivity::class.java)
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
//
//        val channelId = getString(R.string.default_notification_channel_id)
//        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
//        d("channelId","$channelId")
//        d("defaultSoundUri","$defaultSoundUri")
//        d("pendingIntent","$pendingIntent")
//
//        val notificationBuilder = NotificationCompat.Builder(this, channelId)
//            .setSmallIcon(R.drawable.ic_account)
//            .setContentTitle(getString(R.string.app_name))
//            .setContentText(message)
//            .setAutoCancel(true)
//            .setSound(defaultSoundUri)
//            .setContentIntent(pendingIntent)
//
//        d("notificationBuilder","$notificationBuilder")
//
//
//        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//
//        d("notificationManager","$notificationManager")
//
//        // Since Android Oreo notification channel is needed.
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val channel = NotificationChannel(channelId, "Channel human readable title", NotificationManager.IMPORTANCE_DEFAULT)
//            notificationManager.createNotificationChannel(channel)
//        }
//
//        notificationManager.notify(0, notificationBuilder.build())
//    }

    @SuppressLint("StringFormatInvalid")
    private fun handleUserJoin(title: String?, msg: String) {
//        val data = remoteMessage.data
//        val username = data["username"]
//        val message = getString(R.string.user_join_message, username)
//        sendNotification(message)


//        if (remoteMessage.notification != null) {
//            showNotification(remoteMessage.notification?.title, remoteMessage.notification?.body)
//        } else {
//            showNotification(remoteMessage.data["title"], remoteMessage.data["message"])
//        }


        showNotifications(title!!,msg)
    }

//    private fun handleNotificationPayload(notification: RemoteMessage.Notification) {
//        val title = notification.title
//        val message = notification.body
//        val imageUrl = notification.imageUrl
//
//        if (imageUrl != null) {
//
//        }
//
//        sendNotification(title, message)
//    }

//    private fun sendNotification(title: String?, message: String?) {
//        val intent = Intent(this, HomeActivity::class.java)
//        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_MUTABLE)
//        val CHANNEL_ID ="Notification"
//        // Create a notification builder with the required fields
//        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
//            .setSmallIcon(R.drawable.ic_account)
//            .setContentTitle(title)
//            .setContentText(message)
//            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//            .setContentIntent(pendingIntent)
//            .setAutoCancel(true)
//
//        // Show the notification
//        val notificationId = System.currentTimeMillis().toInt()
//
//        with(NotificationManagerCompat.from(this)) {
//            notify(notificationId, builder.build())
//        }
//    }
}