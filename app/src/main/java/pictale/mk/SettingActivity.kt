package pictale.mk

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Log.d
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_setting.*
import pictale.mk.auth.*
import pictale.mk.auth.responses.LoggedResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SettingActivity : AppCompatActivity() {

    @SuppressLint("InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)


        back_page_click.setOnClickListener {
            startActivity(Intent(this,HomeActivity::class.java))
        }
        btn_change.setOnClickListener {
            startActivity(Intent(this,ChangeInfoActivity::class.java))
        }

        btn_change_password.setOnClickListener {
            startActivity(Intent(this,ChangePasswordActivity::class.java))

        }
    }

    override fun onStart() {
        super.onStart()
        val sharedPreferences = getSharedPreferences("preferences", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("token", "")
        d("Token>>>","$token")
        updateUI()

    }

    private fun updateUI() {
        val sharedPreferences = getSharedPreferences("preferences", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("token", "")
        d("Tokenot od home","$token")
        if (token==""){
            startActivity(Intent(this,LoginActivity::class.java))
        }
        showProfile()

    }

    private fun showProfile() {
        val sharedPreferences = getSharedPreferences("preferences", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("token", "")
        val api = RetrofitInstance.getRetrofitInstance().create(API::class.java)

        d("Profile_token-->", "$token")

        api.getClient("Bearer $token").enqueue(object :Callback<LoggedResponse>{
            override fun onResponse(call: Call<LoggedResponse>, response: Response<LoggedResponse>) {
                d("Profile_response-->", "${response.body()}")
                if (response.code()==200) {

                    d("in Response-->", response.body()?.email.toString())
                    val email=response.body()?.email.toString()
                    val firstName=response.body()?.firstName.toString()
                    val lastName=response.body()?.lastName.toString()
                    your_email.setText("Email: $email")
                    your_first_name.setText("First Name: $firstName")
                    your_last_name.setText("Last Name: $lastName")

                }
            }

            override fun onFailure(call: Call<LoggedResponse>, t: Throwable) {
                Toast.makeText(this@SettingActivity, t.message, Toast.LENGTH_SHORT)
                    .show()
                t.message?.let { Log.d("Login_failure-->", it) }

            }
        })
    }


}