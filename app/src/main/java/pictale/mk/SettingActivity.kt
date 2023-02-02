package pictale.mk

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Log.d
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_setting.*
import pictale.mk.auth.API
import pictale.mk.auth.LoggedResponse
import pictale.mk.auth.RetrofitInstance
import pictale.mk.auth.TokenResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Suppress("NAME_SHADOWING")
class SettingActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)


        back_page_click.setOnClickListener {
            startActivity(Intent(this,HomeActivity::class.java))
        }
//        btn_change_password.setOnClickListener {
//
//        }

    }





}