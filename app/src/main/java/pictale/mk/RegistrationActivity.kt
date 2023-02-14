package pictale.mk

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log.d
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_registration.*
import pictale.mk.auth.API
import pictale.mk.auth.responses.ResponseBody
import pictale.mk.auth.RetrofitInstance
import pictale.mk.auth.Signup
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RegistrationActivity : AppCompatActivity() {

    private lateinit var progresDialog: ProgressDialog
    private val sharedPreferences by lazy {
        getSharedPreferences("preferences", Context.MODE_PRIVATE)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        progresDialog= ProgressDialog(this)
        progresDialog.setTitle("Please wait")
        progresDialog.setMessage("Loading please wait")
        progresDialog.setCancelable(false)

        alreadyHaveAccount.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
        btn_registration.setOnClickListener {
            val firstName = firstName_registration.text.toString()
            val lastName = lastName_registration.text.toString()
            val email = email_registration.text.toString()
            val pass = password_registration.text.toString()
            val cnfPass = cnfpassword_registration.text.toString()
            if (firstName.isEmpty()) {
                firstName_registration.error = "Email Required!!!"
                firstName_registration.requestFocus()
            } else if (lastName.isEmpty()) {
                lastName_registration.error = "Email Required!!!"
                lastName_registration.requestFocus()
            } else if (email.isEmpty()) {
                email_registration.error = "Email Required!!!"
                email_registration.requestFocus()
            } else if (pass.isEmpty()) {
                password_registration.error = "Password Required!!!"
                password_registration.requestFocus()
            } else if (cnfPass.isEmpty()) {
                cnfpassword_registration.error = "Password Required!!!"
                cnfpassword_registration.requestFocus()
            }else if (cnfPass!=pass) {
                cnfpassword_registration.error = "Its not confirmed password!!!"
                cnfpassword_registration.requestFocus()
            } else {
                signup(email,
                    firstName,
                    lastName,
                    pass)
            }
        }


    }

    private fun signup( email: String,
                        firstName: String,
                       lastName: String,
                        password: String){
        val api = RetrofitInstance.getRetrofitInstance().create(API::class.java)
        val signupDTO= Signup(email,firstName,lastName,password)

        api.signup(signupDTO)
            .enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

                firstName_registration.setText("")
                lastName_registration.setText("")
                email_registration.setText("")
                password_registration.setText("")
                cnfpassword_registration.setText("")

                d("in F-->","${t.message}")

            }
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                d("R-->","${response.body()}")
                firstName_registration.setText("")
                lastName_registration.setText("")
                email_registration.setText("")
                password_registration.setText("")
                cnfpassword_registration.setText("")
                val token = response.body()?.token.toString()
//                val refreshToken = response.body()?.refreshToken.toString()
//                val expiresIn = response.body()?.expiresIn.toString()
                val sharedPreferences = getSharedPreferences("preferences", Context.MODE_PRIVATE)
                sharedPreferences.edit().putString("token", token).apply()
//                sharedPreferences.edit().putString("refreshToken", refreshToken).apply()
//                sharedPreferences.edit().putString("expiresIn", expiresIn).apply()

                startActivity(Intent(this@RegistrationActivity, HomeActivity::class.java))

//                if (response.isSuccessful) {
//                    firstName_registration.setText("")
//                    lastName_registration.setText("")
//                    email_registration.setText("")
//                    password_registration.setText("")
//                    cnfpassword_registration.setText("")
//                    val token = response.body()?.token.toString()
//                    val refreshToken = response.body()?.refreshToken.toString()
//                    val expiresIn = response.body()?.expiresIn.toString()
//                    val sharedPreferences = getSharedPreferences("preferences", Context.MODE_PRIVATE)
//                    sharedPreferences.edit().putString("token", token).apply()
//                    sharedPreferences.edit().putString("refreshToken", refreshToken).apply()
//                    sharedPreferences.edit().putString("expiresIn", expiresIn).apply()
//
//                    startActivity(Intent(this@RegistrationActivity, HomeActivity::class.java))
//
//                }
//                else{
//                    Toast.makeText(this@RegistrationActivity, "${response.body()}", Toast.LENGTH_SHORT)
//                        .show()
//                }
            }
        })
    }
}