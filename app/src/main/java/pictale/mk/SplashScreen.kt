package pictale.mk

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log.d
import android.view.WindowManager

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

        val sharedPreferences = getSharedPreferences("preferences", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("token", "")
        d("token_spash","$token")
        if(token!=null){
            isUserLoggedIn = true
        }

        return isUserLoggedIn
    }
}
