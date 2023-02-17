package pictale.mk

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Log.d
import kotlinx.android.synthetic.main.activity_add_event.*
import pictale.mk.auth.AuthToken
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

        btn_back.setOnClickListener {
            startActivity(Intent(this,HomeActivity::class.java))
        }
        add.setOnClickListener {
            val name = add_name.text.toString()
            var collaboration ="ANYONE"
            val anyone=ROLE_ANYONE.isChecked
            val editors=ROLE_EDITORS.isChecked
            val self=ROLE_SELF.isChecked
            if (anyone){collaboration="ANYONE"}
            if (editors){collaboration="EDITORS_ONLY"}
            if (self){collaboration="ONLY_ME?"}
            val description = add_description.text.toString()
            val array = add_tags.text.toString()
            val tags=array.split(" ")
            val public = add_isPublic.isChecked()
            val location = add_location.text.toString()

            val token =AuthToken.get(this)

            val api = RetrofitInstanceV2.getRetrofitInstance().create(APIv2::class.java)
            val addDTO= AddEventBody(collaboration, description, public, location, name, tags)

            api.addEvent("Bearer $token",addDTO)
                .enqueue(object : Callback<ResponseAddEvent> {
                override fun onResponse(
                    call: Call<ResponseAddEvent>,
                    response: Response<ResponseAddEvent>
                ) {
                    d("response_body","${response.body()}")
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
        updateUI()

    }

    private fun updateUI() {
        val token = AuthToken.get(this)
        if (token==""){
            startActivity(Intent(this,LoginActivity::class.java))
        }
    }
}