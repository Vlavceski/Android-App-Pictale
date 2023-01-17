package pictale.mk

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_registration.*

class LoginActivity : AppCompatActivity() {

    private lateinit var  mAuth: FirebaseAuth
    private lateinit var progresDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mAuth= FirebaseAuth.getInstance()
        progresDialog=ProgressDialog(this)
        progresDialog.setTitle("Please wait")
        progresDialog.setCancelable(false)

        btn_login.setOnClickListener {
            var email=email_login.text.toString()
            var pass=password_login.text.toString()
            if (email.isEmpty()){
                email_login.error="Email Required!!!"
                email_login.requestFocus()
            }
            else if(pass.isEmpty()){
                password_login.error="Password Required!!!"
                password_login.requestFocus()
            }
            else{
                progresDialog.show()
                //Login user
                mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener {
                    progresDialog.dismiss()
                    if (it.isSuccessful){
                        Toast.makeText(this,"User login Successfully", Toast.LENGTH_SHORT).show()
                        finish()
                        startActivity(Intent(this,HomeActivity::class.java))
                    }
                    else{
                        Toast.makeText(
                            this,
                            "User login failed due to ${it.exception}",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                }

            }
        }

        txt_registration.setOnClickListener {
            startActivity(Intent(this, RegistrationActivity::class.java))
        }

    }


    override fun onStart() {
        super.onStart()
        val currentUser=mAuth.currentUser
        updateUI(currentUser)
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser!=null){
            finish()
            startActivity(Intent(this,HomeActivity::class.java))
        }
    }
}