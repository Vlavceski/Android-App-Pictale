package pictale.mk

import android.Manifest.permission.POST_NOTIFICATIONS
import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.Log.d
import android.view.Menu
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.ktx.messaging
import kotlinx.android.synthetic.main.activity_home.*
import pictale.mk.auth.AuthToken
import pictale.mk.fragments.AllEventsFragment
import pictale.mk.fragments.FavEventsFragment
import pictale.mk.fragments.HighlightsFragment
import pictale.mk.fragments.MyEventsFragment


class HomeActivity : AppCompatActivity() {


    @SuppressLint("StringFormatInvalid")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)


///////////////////
        FirebaseApp.initializeApp(this)
//        Firebase.messaging.subscribeToTopic("userRequestAccess")
//            .addOnCompleteListener { task ->
//                var msg = "Subscribed"
//                if (!task.isSuccessful) {
//                    msg = "Subscribe failed"
//                }
//                Log.d(TAG, msg)
//                Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
//            }
//        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
//            if (!task.isSuccessful) {
//                Log.w("TAG", "Fetching FCM registration token failed", task.exception)
//                return@OnCompleteListener
//            }
//
//            // Get new FCM registration token
//            val token = task.result
//
//            // Log and toast
//            val msg = getString(R.string.msg_token_fmt, token)
//            Log.d("TAG", msg)
//            Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
//        })

//        Firebase.messaging.isAutoInitEnabled = true
/////////////////////

        add_event.setOnClickListener {
            startActivity(Intent(this,AddEventActivity::class.java))
        }

        imageView5.setOnClickListener {
            sendMessage()
        }

        toolbar_click.setOnMenuItemClickListener{
            when(it.itemId){
                R.id.profile_menu -> startActivity(Intent(this,SettingActivity::class.java))
                R.id.logout_menu -> logout()
                else -> {true}
            }
            true
        }


        replaceFragment(AllEventsFragment())
        nav_click.setOnItemSelectedListener {

            when(it.itemId){
                R.id.all_events -> replaceFragment(AllEventsFragment())
                R.id.my_events -> replaceFragment(MyEventsFragment())
                R.id.fav_events -> replaceFragment(FavEventsFragment())
                R.id.highlights -> replaceFragment(HighlightsFragment())
                else -> {true}
            }
            true
        }

    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // FCM SDK (and your app) can post notifications.
        } else {
            // TODO: Inform user that that your app will not show notifications.
        }
    }

    private fun askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                // FCM SDK (and your app) can post notifications.
            } else if (shouldShowRequestPermissionRationale(POST_NOTIFICATIONS)) {
                // TODO: display an educational UI explaining to the user the features that will be enabled
                //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
                //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
                //       If the user selects "No thanks," allow the user to continue without notifications.
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(POST_NOTIFICATIONS)
            }
        }
    }



    private fun sendMessage() {

//        val firebaseMessaging = FirebaseMessaging.getInstance()
//
//        val message = Message.builder()
//            .setNotification(RemoteMessage.Notification("Title", "Body"))
//            .setToken("device-token")
//            .build()
//
//        firebaseMessaging.send(message)


//        val token=AuthToken.get(this)
//        val api=RetrofitInstanceV4.getRetrofitInstance().create(APIv4::class.java)
//        api.sendNotification("Bearer $token").enqueue(object :Callback<List<FcmResponse>>{
//            override fun onResponse(
//                call: Call<List<FcmResponse>>,
//                response: Response<List<FcmResponse>>
//            ) {
//                d("Success","FTM")
//            }
//
//            override fun onFailure(call: Call<List<FcmResponse>>, t: Throwable) {
//               d("Failure","${t.message}")
//            }
//
//        })
    }




    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent?.extras != null) {
            val message = intent.extras?.getString("message")
            if (message != null) {
                // Handle the message
                showMessage(message)
            }
        }
    }

    private fun showMessage(message: String) {
        // Display the message to the user
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun logout() {
        val sharedPreferences = getSharedPreferences("preferences", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("token", null).apply()
        updateUI()
    }


    private fun replaceFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame, fragment)
        transaction.commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.main,menu)
        return true
    }

    override fun onStart() {
        super.onStart()
        updateUI()
    }

    private fun updateUI() {
        val token=AuthToken.get(this)
        d("token","$token")
        if (token==null){
            startActivity(Intent(this,LoginActivity::class.java))
        }

    }

}



