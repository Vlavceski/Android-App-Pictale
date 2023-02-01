package pictale.mk

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_registration.*
import pictale.mk.auth.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var progresDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        progresDialog=ProgressDialog(this)
        progresDialog.setTitle("Please wait")
        progresDialog.setCancelable(false)


        btn_login.setOnClickListener {
            val email = email_login.text.toString()
            val password = password_login.text.toString()
            if (email.isEmpty()){
                email_login.error="Email Required!!!"
                email_login.requestFocus()
            }
            else if(password.isEmpty()){
                password_login.error="Password Required!!!"
                password_login.requestFocus()
            }
                else {
                        signin(email,password)
                    }

        }


        txt_registration.setOnClickListener {
            startActivity(Intent(this, RegistrationActivity::class.java))
        }



    }
    fun signin(email: String,password: String){

        val api = RetrofitInstance.getRetrofitInstance().create(API::class.java)

        api.signin(email, password).enqueue(object :Callback<TokenResponse>{
            override fun onResponse(call: Call<TokenResponse>, response: Response<TokenResponse>) {
                Log.d("Login_response-->", "${response.body()}")
                    if (response.isSuccessful) {
                        Toast.makeText(this@LoginActivity, "Registration success!", Toast.LENGTH_SHORT)
                            .show()
                        Log.d("in R-->", "${response.body()}")
                        Log.d("in R-->", "${response.code()}")
                        Log.d("in R-->", response.body()?.token.toString())
                        email_login.setText("")
                        password_login.setText("")


                    }
                    else{
                        Toast.makeText(this@LoginActivity, "${response.body()}", Toast.LENGTH_SHORT)
                            .show()
                    }
                 }

            override fun onFailure(call: Call<TokenResponse>, t: Throwable) {
                Toast.makeText(this@LoginActivity, t.message, Toast.LENGTH_SHORT)
                        .show()
                    t.message?.let { Log.d("Login_failure-->", it) }
                    email_login.setText("")
                    password_login.setText("")
                }
        })
    }

}