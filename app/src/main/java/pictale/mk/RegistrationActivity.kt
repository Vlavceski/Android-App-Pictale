package pictale.mk

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log.d
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_registration.*
import pictale.mk.api.ApiInterface
import pictale.mk.api.RetrofitInstance
import pictale.mk.auth.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RegistrationActivity : AppCompatActivity() {

//    private var URL="http://88.85.111.72:37990/api/v1/"
    private lateinit var progresDialog: ProgressDialog
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
        val retIn = RetrofitInstance.getRetrofitInstance().create(ApiInterface::class.java)
//        val registerInfo = Signup(email,firstName,lastName,password)

        retIn.registerUser(email,firstName,lastName,password)
            .enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@RegistrationActivity, t.message, Toast.LENGTH_SHORT)
                    .show()
            }
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                d("-->","${response.body()}")
                if (response.code() == 201) {
                    Toast.makeText(this@RegistrationActivity, "Registration success!", Toast.LENGTH_SHORT)
                        .show()

                }
                else{
                    Toast.makeText(this@RegistrationActivity, "Registration failed!", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        })
    }



}