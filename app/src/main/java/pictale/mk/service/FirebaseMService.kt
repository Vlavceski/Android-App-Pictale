package pictale.mk.service

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.firebase.messaging.ktx.remoteMessage
import pictale.mk.HomeActivity
import pictale.mk.R

//const val  channelId="NotificationChannel"
//const val  channelName="NameChannel"

class FirebaseMService :FirebaseMessagingService(){
    override fun onNewToken(token: String) {
        // Handle token refresh
        Log.d("TAG", "Refreshed token: $token")
    }

    companion object {
        private const val TAG = "MyFirebaseMessagingService"
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // Handle new message
        val message = remoteMessage.data["message"]
        val intent = Intent(this, HomeActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            putExtra("message", message)
        }
        startActivity(intent)
    }
//    override fun onMessageReceived(remoteMessage: RemoteMessage) {
//        super.onMessageReceived(remoteMessage)
//
//        // handle the received message
//        val notificationTitle = remoteMessage.notification?.title
//        val notificationBody = remoteMessage.notification?.body
//        // extract other data from the message
//
//        // show the notification to the user
//        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        val channelId = getString(R.string.default_notification_channel_id)
//        val notificationBuilder = NotificationCompat.Builder(this, channelId)
//            .setSmallIcon(R.drawable.logo_no_background)
//            .setContentTitle(notificationTitle)
//            .setContentText(notificationBody)
//            .setAutoCancel(true)
//            .setPriority(NotificationCompat.PRIORITY_HIGH)
//        // customize the notification further if needed
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            // create the default notification channel for API 26+
//            val channel = NotificationChannel(channelId, "Default Channel", NotificationManager.IMPORTANCE_HIGH)
//            notificationManager.createNotificationChannel(channel)
//        }
//
//        val notificationId = 1 // unique ID for each notification
//        notificationManager.notify(notificationId, notificationBuilder.build())
//    }



//    override fun onMessageReceived(remoteMessage: RemoteMessage) {
//        // ...
//
//        // TODO(developer): Handle FCM messages here.
//        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
//        Log.d("TAG", "From: ${remoteMessage.from}")
//
//        // Check if message contains a data payload.
//        if (remoteMessage.data.isNotEmpty()) {
//            Log.d("TAG", "Message data payload: ${remoteMessage.data}")
//
//            if (/* Check if data needs to be processed by long running job */ true) {
//                // For long-running tasks (10 seconds or more) use WorkManager.
//                scheduleJob()
//            } else {
//                // Handle message within 10 seconds
//                handleNow()
//            }
//        }
//
//        // Check if message contains a notification payload.
//        remoteMessage.notification?.let {
//            Log.d(TAG, "Message Notification Body: ${it.body}")
//        }
//
//        // Also if you intend on generating your own notifications as a result of a received FCM
//        // message, here is where that should be initiated. See sendNotification method below.
//    }




//    override fun onMessageReceived(message: RemoteMessage) {
//        if (message.getNotification()!=null) {
//            genNotification(message.notification!!.title!!,message.notification!!.body!!)
//        }
//    }
//    @SuppressLint("UnspecifiedImmutableFlag")
//    fun genNotification(title:String, message: String){
//        val intent= Intent(this,HomeActivity::class.java)
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//
//        val pendingIntent=PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT)
//
//        var builder:NotificationCompat.Builder=NotificationCompat.Builder(applicationContext, channelId)
//            .setSmallIcon(R.drawable.logo_no_background)
//            .setAutoCancel(true)
//            .setVibrate(longArrayOf(1000,1000,1000,1000))
//            .setContentIntent(pendingIntent)
//
//        builder=builder.setContent(getRemoteView(title,message))
//
//        val notificationManager=getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
//            val notificationChannel=NotificationChannel(channelId, channelName,NotificationManager.IMPORTANCE_HIGH)
//            notificationManager.createNotificationChannel(notificationChannel)
//        }
//        notificationManager.notify(0,builder.build())
//    }
//
//    private fun getRemoteView(title: String, message: String): RemoteViews {
//        val remoteViews= RemoteViews("pictale.mk",R.layout.notification)
//        remoteViews.setTextViewText(R.id.titleMsg,title)
//        remoteViews.setTextViewText(R.id.descriptionMsg,message)
//        remoteViews.setImageViewResource(R.id.imageMsg,R.drawable.logo_no_background)
//        return remoteViews
//    }

}