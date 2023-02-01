package pictale.mk

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log.d
import android.view.WindowManager
import pictale.mk.auth.API
import pictale.mk.auth.LoggedResponse
import pictale.mk.auth.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@SuppressLint("CustomSplashScreen")
@Suppress("DEPRECATION")
class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        if (isUserLoggedIn()) {

            Handler(Looper.getMainLooper()).postDelayed({
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish()
            }, 2000) } else {

            Handler(Looper.getMainLooper()).postDelayed({
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }, 2000)

            }

    }

    private fun isUserLoggedIn(): Boolean {
        var isUserLoggedIn = false

        val api = RetrofitInstance.getRetrofitInstance().create(API::class.java)
//        val token=ResponseBody.body()
        val sharedPreferences = getSharedPreferences("preferences", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("token", "")
        api.getClient(token).enqueue(object : Callback<LoggedResponse> {
            override fun onResponse(call: Call<LoggedResponse>, response: Response<LoggedResponse>) {
                if (response.isSuccessful) {
                    isUserLoggedIn = true
                }
            }

            override fun onFailure(call: Call<LoggedResponse>, t: Throwable) {
                d("Failure",t.message.toString())
            }
        })

        return isUserLoggedIn
    }
}
