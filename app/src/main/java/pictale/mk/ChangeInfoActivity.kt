package pictale.mk

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_change_info.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_setting.*
import kotlinx.android.synthetic.main.popup_change_info.*
import pictale.mk.auth.API
import pictale.mk.auth.ChangeNameOrSurname
import pictale.mk.auth.LoggedResponse
import pictale.mk.auth.RetrofitInstance
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
        showProfile()
    }

    private fun showProfile() {
        val sharedPreferences = getSharedPreferences("preferences", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("token", "")
        val api = RetrofitInstance.getRetrofitInstance().create(API::class.java)

        Log.d("Profile_token-->", "$token")

        api.getClient("Bearer $token").enqueue(object : Callback<LoggedResponse> {
            override fun onResponse(call: Call<LoggedResponse>, response: Response<LoggedResponse>) {
                Log.d("Profile_response-->", "${response.body()}")
                if (response.isSuccessful) {

                    Log.d("in R-->", response.body()?.email.toString())
                    val firstName=response.body()?.firstName.toString()
                    val lastName=response.body()?.lastName.toString()
                    changeFirstNameORLastName(firstName,lastName)

                }
            }

            override fun onFailure(call: Call<LoggedResponse>, t: Throwable) {
                Toast.makeText(this@SettingActivity, t.message, Toast.LENGTH_SHORT)
                    .show()
                t.message?.let { Log.d("Login_failure-->", it) }
                email_login.setText("")
                password_login.setText("")
            }
        })
    }

    private fun changeFirstNameORLastName(firstName: String, lastName: String) {
        val inputName = firstName_change.text.toString()
        val inputSurname = lastName_change.text.toString()
        var outputName:String?
        val outputSurname:String?
        outputName=firstName
        if(inputName!=firstName && !inputName.isEmpty()){
            outputName=inputName
        }
        else if(inputSurname){

        }


        val api = RetrofitInstance.getRetrofitInstance().create(API::class.java)

        Log.d("Profile_token-->", "$token")
        var changeDTO_n_s=ChangeNameOrSurname(firstName,lastName)
        api.getClient("Bearer $token").enqueue(object : Callback<LoggedResponse> {
            override fun onResponse(call: Call<LoggedResponse>, response: Response<LoggedResponse>) {
                Log.d("Profile_response-->", "${response.body()}")
                if (response.isSuccessful) {

                    Log.d("in R-->", response.body()?.email.toString())
                    val firstName=response.body()?.firstName.toString()
                    val lastName=response.body()?.lastName.toString()
                    changeFirstNameORLastName(firstName,lastName)

                }
            }

            override fun onFailure(call: Call<LoggedResponse>, t: Throwable) {
                Toast.makeText(this@SettingActivity, t.message, Toast.LENGTH_SHORT)
                    .show()
                t.message?.let { Log.d("Login_failure-->", it) }
                email_login.setText("")
                password_login.setText("")
            }
        })
    }

}