package pictale.mk

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Log.d
import android.view.Gravity
import android.view.WindowManager
import android.widget.PopupWindow
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_setting.*
import kotlinx.android.synthetic.main.popup_change_info.*
import pictale.mk.auth.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Suppress("NAME_SHADOWING")
class SettingActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)


        back_page_click.setOnClickListener {
            startActivity(Intent(this,HomeActivity::class.java))
        }
        btn_change.setOnClickListener {
            val popupWindow = PopupWindow(this)

            val view = layoutInflater.inflate(R.layout.popup_change_info, null)
            popupWindow.contentView = view

            popupWindow.width = WindowManager.LayoutParams.WRAP_CONTENT
            popupWindow.height = WindowManager.LayoutParams.WRAP_CONTENT

            popupWindow.isFocusable = true

            popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)
            close_popup.setOnClickListener {
                popupWindow.dismiss()
            }
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
                if (response.isSuccessful) {

                    d("in R-->", response.body()?.email.toString())
                    val email=response.body()?.email.toString()
                    val firstName=response.body()?.firstName.toString()
                    val lastName=response.body()?.lastName.toString()
                    val pictureUrl=response.body()?.pictureUrl.toString()
                    your_email.setText("Email: $email")
                    your_first_name.setText("First Name: $firstName")
                    your_last_name.setText("Last Name: $lastName")

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