package pictale.mk

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var progresDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        progresDialog=ProgressDialog(this)
        progresDialog.setTitle("Please wait")
        progresDialog.setCancelable(false)


        btn_login.setOnClickListener {
//            if (email.isEmpty()){
//                email_login.error="Email Required!!!"
//                email_login.requestFocus()
//            }
//            else if(pass.isEmpty()){
//                password_login.error="Password Required!!!"
//                password_login.requestFocus()
//            }
//                else {
//
//
//        }
        }





        txt_registration.setOnClickListener {
            startActivity(Intent(this, RegistrationActivity::class.java))
        }



    }

}