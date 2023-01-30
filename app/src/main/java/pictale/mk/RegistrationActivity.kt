package pictale.mk

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log.d
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_registration.*
import okhttp3.OkHttpClient

import pictale.mk.api.API
import pictale.mk.model.Signup
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RegistrationActivity : AppCompatActivity() {

    private var URL="http://88.85.111.72:37990/api/v1/"
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
            Action()
        }

    }
    private fun Action(){
        val firstName=firstName_registration.toString().trim()
        val lastName=lastName_registration.toString().trim()
        val email=email_registration.toString().trim()
        val pass=password_registration.toString().trim()
        val cnfPass=cnfpassword_registration.toString().trim()

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
        }
//        else if (cnfPass!=pass) {
//            cnfpassword_registration.error = "Its not confirmed password!!!"
//            cnfpassword_registration.requestFocus()
//        }
        else {

            val okHttpClient= OkHttpClient.Builder()
                .addInterceptor {
                        chain ->
                    val original=chain.request()
                    val requestBuilder=original.newBuilder()
                        .addHeader("Authorization","1")
                        .method(original.method, original.body)

                    val request=requestBuilder.build()
                    chain.proceed(request)
                }.build()

            val retrofit = Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()

            var api= retrofit.create(API::class.java)


            api.createUser(email,firstName,lastName,pass)
                .enqueue(object: Callback<Signup> {
                override fun onResponse(
                    call: Call<Signup>,
                    response: Response<Signup>
                ) {
                    d("Reg-->","$response")
                    email_registration.setText("")
                    firstName_registration.setText("")
                    lastName_registration.setText("")
                    password_registration.setText("")
                    cnfpassword_registration.setText("")

                    toHomeActivity()
                    Toast.makeText(applicationContext,"Success",Toast.LENGTH_LONG).show()
                }

                override fun onFailure(call: Call<Signup>, t: Throwable) {
                    Toast.makeText(applicationContext,t.message,Toast.LENGTH_LONG).show()
                    email_registration.setText("")
                    firstName_registration.setText("")
                    lastName_registration.setText("")
                    password_registration.setText("")
                    cnfpassword_registration.setText("")

                }

            })
        }

    }
    fun toHomeActivity(){
        startActivity(Intent(this, HomeActivity::class.java))
    }


}