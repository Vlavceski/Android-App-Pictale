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

            d("received--","{$remoteMessage.notification?.title} --- ${remoteMessage.notification?.body!!}")
            when(remoteMessage.data["type"]) {
                "userRequestAccess" -> handleUserJoin(remoteMessage.notification?.title,remoteMessage.notification?.body!!)
//                "uploadFile" -> handleNewPhoto(remoteMessage)
//                "newUser" -> handleNewUser(remoteMessage)
                else -> handleUnknownMessage(remoteMessage)
            }
        }

        if (remoteMessage.notification != null) {
            Log.d(TAG, "Message Notification Body: ${remoteMessage.notification!!.body}")
        }
    }

    private fun handleUnknownMessage(remoteMessage: RemoteMessage) {
            Log.w(TAG, "Received an unknown message: ${remoteMessage.data}")
            val message = getString(R.string.default_message)
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun showNotifications(title:String, remoteMessage: String?) {
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


}