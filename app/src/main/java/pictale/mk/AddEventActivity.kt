package pictale.mk

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Log.d
import kotlinx.android.synthetic.main.activity_add_event.*
import pictale.mk.auth.API
import pictale.mk.auth.RetrofitInstance
import pictale.mk.auth.Signin
import pictale.mk.auth.responses.TokenResponse
import pictale.mk.events.APIv2
import pictale.mk.events.AddEventBody
import pictale.mk.events.ResponseAddEvent
import pictale.mk.events.RetrofitInstanceV2
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddEventActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_event)

        add.setOnClickListener {
            var name = add_name.text.toString()
            var collaboration = add_collaboration.text.toString()
            var description = add_description.text.toString()
            var tags = add_tags.text.toString()
            var public = add_isPublic.isChecked()
            var location = add_location.text.toString()
            val sharedPreferences = getSharedPreferences("preferences", Context.MODE_PRIVATE)
            val token = sharedPreferences.getString("token", "")

            val api = RetrofitInstanceV2.getRetrofitInstance().create(APIv2::class.java)
            val addDTO= AddEventBody(name,collaboration,description,tags,public,location)
            api.addEvent("Bearer $token",addDTO).enqueue(object : Callback<ResponseAddEvent> {
                override fun onResponse(
                    call: Call<ResponseAddEvent>,
                    response: Response<ResponseAddEvent>
                ) {
                    startActivity(Intent(this@AddEventActivity,HomeActivity::class.java))
                }

                override fun onFailure(call: Call<ResponseAddEvent>, t: Throwable) {
                d("F-->","Failure")
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
        Log.d("Tokenot od AddEvent", "$token")
        if (token==""){
            startActivity(Intent(this,LoginActivity::class.java))
        }
    }
}