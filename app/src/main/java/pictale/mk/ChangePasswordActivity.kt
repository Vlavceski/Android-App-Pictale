package pictale.mk

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Log.d
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_change_password.*
import pictale.mk.auth.*
import pictale.mk.auth.responses.ResponseUpdatePassword
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChangePasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        btn_close.setOnClickListener {
            startActivity(Intent(this@ChangePasswordActivity,SettingActivity::class.java))
        }
        btn_change_password.setOnClickListener {
            val inputNewPassword=change_newPassword.text.toString()
            val inputOldPassword=change_oldPassword.text.toString()

            val sharedPreferences = getSharedPreferences("preferences", Context.MODE_PRIVATE)
            val token = sharedPreferences.getString("token", "")
            val api = RetrofitInstance.getRetrofitInstance().create(API::class.java)

            d("ChangeP_token-->", "$token")
            val updatePassword=UpdatePassword(inputNewPassword,inputOldPassword)

            api.updatePassword("Bearer $token",updatePassword).enqueue(object : Callback<ResponseUpdatePassword>{
                override fun onResponse(
                    call: Call<ResponseUpdatePassword>,
                    response: Response<ResponseUpdatePassword>
                ) {
                    d("Response-update: ","${response.body()}")
                    if(response.code()==200){
                        d("Response","Code -Success")
                        startActivity(Intent(this@ChangePasswordActivity,SettingActivity::class.java))
                        change_newPassword.setText("")
                        change_oldPassword.setText("")
                    }
                }

                override fun onFailure(call: Call<ResponseUpdatePassword>, t: Throwable) {
                    Toast.makeText(this@ChangePasswordActivity,"Failure",Toast.LENGTH_LONG).show()

                }

            })

        }
    }






    override fun onStart() {
        super.onStart()
        val sharedPreferences = getSharedPreferences("preferences", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("token", "")
        Log.d("Token>>>", "$token")
        updateUI()

    }
    private fun updateUI() {
        val sharedPreferences = getSharedPreferences("preferences", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("token", "")
        Log.d("Tokenot od home", "$token")
        if (token==""){
            startActivity(Intent(this,LoginActivity::class.java))
        }
    }


}