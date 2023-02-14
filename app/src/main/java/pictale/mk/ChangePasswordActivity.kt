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

            val token = AuthToken.get(this)
            val api = RetrofitInstance.getRetrofitInstance().create(API::class.java)

            val updatePassword=UpdatePassword(inputNewPassword,inputOldPassword)

            api.updatePassword("Bearer $token",updatePassword).enqueue(object : Callback<ResponseUpdatePassword>{
                override fun onResponse(
                    call: Call<ResponseUpdatePassword>,
                    response: Response<ResponseUpdatePassword>
                ) {
                    if(response.code()==200){
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
        updateUI()

    }
    private fun updateUI() {
        val token=AuthToken.get(this)
        if (token==""){
            startActivity(Intent(this,LoginActivity::class.java))
        }
    }


}