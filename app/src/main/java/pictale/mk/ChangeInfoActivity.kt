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

            val sharedPreferences = getSharedPreferences("preferences", Context.MODE_PRIVATE)
            val token = sharedPreferences.getString("token", "")
            val api = RetrofitInstance.getRetrofitInstance().create(API::class.java)

            Log.d("setting_token-->", "$token")

            api.getClient("Bearer $token").enqueue(object :Callback<LoggedResponse>{
                override fun onResponse(call: Call<LoggedResponse>, response: Response<LoggedResponse>) {
                    Log.d("Profile_response-->", "${response.body()}")
                    if (response.isSuccessful) {
                        Log.d("in R-->", response.body()?.email.toString())
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
                    Toast.makeText(this@ChangeInfoActivity, t.message, Toast.LENGTH_SHORT)
                        .show()
                    t.message?.let { Log.d("Login_failure-->", it) }
                }
            })



        }
    }

    private fun updateInfo(name: String, surname: String) {
        val sharedPreferences = getSharedPreferences("preferences", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("token", "")
        val api = RetrofitInstance.getRetrofitInstance().create(API::class.java)

        d("setting_token-->", "$token")
        val update=UpdateInfo(name,surname)
        api.updateInfo("Bearer $token",update).enqueue(object :Callback<ResponseUpdateInfo>{
            override fun onResponse(call: Call<ResponseUpdateInfo>, response: Response<ResponseUpdateInfo>) {
                d("setting_response-->", "${response.body()}")
                if (response.isSuccessful) {
                    d("Changed- Success "," Ok!!!")
                    startActivity(Intent(this@ChangeInfoActivity, SettingActivity::class.java))

                }
            }

            override fun onFailure(call: Call<ResponseUpdateInfo>, t: Throwable) {
                Toast.makeText(this@ChangeInfoActivity, t.message, Toast.LENGTH_SHORT)
                    .show()
                t.message?.let { Log.d("Login_failure-->", it) }
            }
        })

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