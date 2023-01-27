package pictale.mk

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_registration.*
import pictale.mk.api.API
import pictale.mk.data.User
import pictale.mk.model.Signup
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RegistrationActivity : AppCompatActivity() {
//    private lateinit var mAuth: FirebaseAuth

    private var URL="api-vnesi"
    private lateinit var progresDialog: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
//        mAuth = FirebaseAuth.getInstance()

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



        //Registration
       /* btn_registration.setOnClickListener {
            var firstName = firstName_registration.text.toString()
            var lastName = lastName_registration.text.toString()
            var email = email_registration.text.toString()
            var pass = password_registration.text.toString()
            var cnfPass = cnfpassword_registration.text.toString()
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
                    progresDialog.show()
                    mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
                        progresDialog.dismiss()
                            var user=User()
                            user.firstName=firstName;
                            user.lastName=lastName;
                            user.email=email;
                            user.password=pass;
                            user.authID=mAuth.currentUser!!.uid;
                            user.recordID=""
                            val dbRef=FirebaseDatabase.getInstance().reference.child("USERS")
                            val recordID=dbRef.push().key.toString()
                            user.recordID=recordID;
                            dbRef.child(recordID).setValue(user).addOnCompleteListener {
                                if(it.isSuccessful){
                                    finish()
                                    startActivity(Intent(this, HomeActivity::class.java))
                                }
                                else{
                                    Toast.makeText(
                                        this,
                                        "Register failed due to ${it.exception}",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                        }
                    }
                }


        */


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
        }else if (cnfPass!=pass) {
            cnfpassword_registration.error = "Its not confirmed password!!!"
            cnfpassword_registration.requestFocus()
        } else {

//            Retrofit retrofit=new Retrofit
//                .Builder()
//                .baseUrl(URL)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//            API api=retofit.create(API.class);
//            Call<Signup> call=api.createUser(firstName,lastName,email,password)
//
//            call.enqueue(new Callback<Signup>{
//                onResponse  //startactiviti do home i napravi toast
//
//
//                onFailer
//            })

            


        }

    }

/*
    override fun onStart() {
        super.onStart()
        val currentUser = mAuth.currentUser
        updateUI(currentUser)
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser != null) {
            finish()
            startActivity(Intent(this, HomeActivity::class.java))
        }
    }

 */
}