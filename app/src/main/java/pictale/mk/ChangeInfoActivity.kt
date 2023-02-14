package pictale.mk

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Log.d
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_change_info.*
import pictale.mk.auth.*
import pictale.mk.auth.responses.LoggedResponse
import pictale.mk.auth.responses.ResponseUpdateInfo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChangeInfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_info)
        close_popup.setOnClickListener {
            startActivity(Intent(this,SettingActivity::class.java))
        }
        change_info.setOnClickListener {
            var inputName=firstName_change.text.toString()
            var inputSurname=lastName_change.text.toString()
            var currName:String
            var currSurname:String

            val token = AuthToken.get(this)
            val api = RetrofitInstance.getRetrofitInstance().create(API::class.java)


            api.getClient("Bearer $token").enqueue(object :Callback<LoggedResponse>{
                override fun onResponse(call: Call<LoggedResponse>, response: Response<LoggedResponse>) {
                    if (response.isSuccessful) {
                        currName=response.body()?.firstName.toString()
                        currSurname=response.body()?.lastName.toString()
                        var name:String
                        var surname:String
                        if (inputName!=currName && !inputName.isEmpty()){
                            name=inputName
                        }
                        else {
                            name=currName
                        }
                        if (inputSurname!=currSurname && !inputSurname.isEmpty()){
                            surname=inputSurname
                        }
                        else {
                            surname=currSurname
                        }
                        updateInfo(name,surname)

                    }
                }

                override fun onFailure(call: Call<LoggedResponse>, t: Throwable) {
                    t.message?.let { Log.d("Login_failure-->", it) }
                }
            })



        }
    }

    private fun updateInfo(name: String, surname: String) {
        val token = AuthToken.get(this)

        val api = RetrofitInstance.getRetrofitInstance().create(API::class.java)

        val update=UpdateInfo(name,surname)
        api.updateInfo("Bearer $token",update).enqueue(object :Callback<ResponseUpdateInfo>{
            override fun onResponse(call: Call<ResponseUpdateInfo>, response: Response<ResponseUpdateInfo>) {

                if (response.isSuccessful) {
                    startActivity(Intent(this@ChangeInfoActivity, SettingActivity::class.java))
                }
            }

            override fun onFailure(call: Call<ResponseUpdateInfo>, t: Throwable) {
                t.message?.let { Log.d("Login_failure-->", it) }
            }
        })

    }

    override fun onStart() {
        super.onStart()
        updateUI()

    }
    private fun updateUI() {
        val token = AuthToken.get(this)
        if (token==""){
            startActivity(Intent(this,LoginActivity::class.java))
        }
    }


}