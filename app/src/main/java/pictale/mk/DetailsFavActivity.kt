package pictale.mk

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log.d
import kotlinx.android.synthetic.main.activity_details.back_page_click
import kotlinx.android.synthetic.main.activity_details.location_of_event
import kotlinx.android.synthetic.main.activity_details.name_of_event
import kotlinx.android.synthetic.main.activity_details_fav.*
import pictale.mk.auth.AuthToken
import pictale.mk.events.APIv2
import pictale.mk.events.ResponseDeleteFav
import pictale.mk.events.RetrofitInstanceV2
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailsFavActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details_fav)

        val eventName = intent.getStringExtra("name")
        val eventLocation = intent.getStringExtra("location")
        val eventId = intent.getStringExtra("eventId")
        name_of_event.text = eventName
        location_of_event.text = eventLocation
        back_page_click.setOnClickListener {
            startActivity(Intent(this@DetailsFavActivity,HomeActivity::class.java))
        }
        btn_fav_del.setOnClickListener {
            val token=AuthToken.get(this)
            val api= RetrofitInstanceV2.getRetrofitInstance().create(APIv2::class.java)

            api.deleteEventFav("Bearer $token",eventId!!).enqueue(object :Callback<ResponseDeleteFav>{
                override fun onResponse(
                    call: Call<ResponseDeleteFav>,
                    response: Response<ResponseDeleteFav>
                ) {
                    startActivity(Intent(this@DetailsFavActivity,HomeActivity::class.java))
                }
                override fun onFailure(call: Call<ResponseDeleteFav>, t: Throwable) {
                    d("Failure","${t.message}")
                }

            })

        }

    }

}