package pictale.mk

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.*
import pictale.mk.api.API
import pictale.mk.model.Signup
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginActivity : AppCompatActivity() {

//    private lateinit var  mAuth: FirebaseAuth
    private lateinit var progresDialog: ProgressDialog
//    private lateinit var googleSignInClient: GoogleSignInClient
//
//    companion object {
//        private const val TAG = "GoogleActivity"
//        private const val RC_SIGN_IN = 6
//    }
    private var URL="http://88.85.111.72:37990/api/v1/"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

//        mAuth= FirebaseAuth.getInstance()
//        mAuth = Firebase.auth

        progresDialog=ProgressDialog(this)
        progresDialog.setTitle("Please wait")
        progresDialog.setCancelable(false)


        btn_login.setOnClickListener {
            Action()
        }

      /*  //Google Auth
        btn_google.setOnClickListener {
            signIn()
        }
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
       */
        /*//Login user
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
                mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener {
                    progresDialog.dismiss()
                    if (it.isSuccessful){
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

         */



        txt_registration.setOnClickListener {
            startActivity(Intent(this, RegistrationActivity::class.java))
        }



    }

    private fun Action(){
        val email=email_login.toString().trim()
        val pass=password_login.toString().trim()

        if (email.isEmpty()) {
            email_login.error = "Email Required!!!"
            email_login.requestFocus()
        } else if (pass.isEmpty()) {
            password_login.error = "Password Required!!!"
           password_login.requestFocus()
        }
        else {

            val retrofit = Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            var api= retrofit.create(API::class.java)


            api.loginUser(email,pass)
                .enqueue(object: Callback<Signup> {
                    override fun onResponse(
                        call: Call<Signup>,
                        response: Response<Signup>
                    ) {
                        Log.d("Login-->", "Done!!!")
                        email_login.setText("")
                        password_login.setText("")

                        Toast.makeText(applicationContext,"Success",Toast.LENGTH_LONG).show()
                    }

                    override fun onFailure(call: Call<Signup>, t: Throwable) {
                        Toast.makeText(applicationContext,t.message,Toast.LENGTH_LONG).show()
                        email_login.setText("")
                        password_login.setText("")

                    }
                })
        }
    }



/*
    override fun onStart() {
        super.onStart()
        val currentUser=mAuth.currentUser
        updateUI(currentUser)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)

            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
            }
        }
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent

        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken,null)
        Log.d("Token>> ",idToken)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                   Log.d(TAG, "signInWithCredential:success")
                    val user = mAuth.currentUser
                    updateUI(user)
                } else {
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    updateUI(null)
                }
            }
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser!=null){
            finish()
            startActivity(Intent(this,HomeActivity::class.java))
        }
    }

 */
}