package pictale.mk

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_setting.*

class SettingActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        mAuth = FirebaseAuth.getInstance()

        back_page_click.setOnClickListener {
            startActivity(Intent(this,HomeActivity::class.java))
        }
//        btn_change_password.setOnClickListener {
//
//        }

    }

    override fun onStart() {
        super.onStart()
        val currentUser=mAuth.currentUser
        updateUI(currentUser)
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser==null){
            finish()
            startActivity(Intent(this,LoginActivity::class.java))
        }
        else
        {
            your_email.text="${mAuth.currentUser!!.email}"
            your_first_name.text="${mAuth.currentUser!!.displayName}"
        }


    }
}