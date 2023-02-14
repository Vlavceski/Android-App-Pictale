package pictale.mk

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Log.d
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import kotlinx.android.synthetic.main.activity_login.*
import pictale.mk.auth.*
import pictale.mk.auth.responses.ResponseGoogleLogin
import pictale.mk.auth.responses.TokenResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var progresDialog: ProgressDialog
    private var RC_SIGN_IN=6
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        progresDialog=ProgressDialog(this)
        progresDialog.setTitle("Please wait")
        progresDialog.setCancelable(false)
            //fix delete

        btn_google.setOnClickListener {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
            val googleSignInClient = GoogleSignIn.getClient(this, gso)

            val signInIntent = googleSignInClient.signInIntent

            startActivityForResult(signInIntent, RC_SIGN_IN)

        }


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
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                val token = account?.idToken
                val api= RetrofitInstance.getRetrofitInstance().create(API::class.java)
                val DTOGoogleSignIn= token?.let { GoogleSignIn(it) }
                api.signinWithGoogle(DTOGoogleSignIn!!).enqueue(object : Callback<ResponseGoogleLogin>{
                    override fun onResponse(
                        call: Call<ResponseGoogleLogin>,
                        response: Response<ResponseGoogleLogin>
                    ) {
                        val token=response.body()?.token.toString()
//                        val refreshToken = response.body()?.refreshToken.toString()
//                        val expiresIn = response.body()?.expiresIn.toString()

                        val sharedPreferences = getSharedPreferences("preferences", Context.MODE_PRIVATE)
                        sharedPreferences.edit().putString("token",token).apply()
//                        sharedPreferences.edit().putString("refreshToken",refreshToken).apply()
//                        sharedPreferences.edit().putString("expiresIn",expiresIn).apply()
                        startActivity(Intent(this@LoginActivity,HomeActivity::class.java))
                    }

                    override fun onFailure(call: Call<ResponseGoogleLogin>, t: Throwable) {
                        d("Failure","${t.message}")
                    }

                })
            } catch (e: ApiException) {
            }
        }
    }


    fun signin(email: String,password: String){

        val api = RetrofitInstance.getRetrofitInstance().create(API::class.java)
        val SigninDTO=Signin(email, password)
        api.signin(SigninDTO).enqueue(object :Callback<TokenResponse>{
            override fun onResponse(call: Call<TokenResponse>, response: Response<TokenResponse>) {
                    if (response.isSuccessful) {
                        val token = response.body()?.token.toString()
//                        val refreshToken = response.body()?.refreshToken.toString()
//                        val expiresIn = response.body()?.expiresIn.toString()

                        val sharedPreferences = getSharedPreferences("preferences", Context.MODE_PRIVATE)
                        sharedPreferences.edit().putString("token",token).apply()
//                        sharedPreferences.edit().putString("refreshToken",refreshToken).apply()
//                        sharedPreferences.edit().putString("expiresIn",expiresIn).apply()

                        startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                    }
                    else{
                        Toast.makeText(this@LoginActivity, "${response.body()}", Toast.LENGTH_SHORT)
                            .show()
                    }
                 }

            override fun onFailure(call: Call<TokenResponse>, t: Throwable) {
                Toast.makeText(this@LoginActivity, t.message, Toast.LENGTH_SHORT)
                        .show()
                    email_login.setText("")
                    password_login.setText("")
                }
        })
    }




}