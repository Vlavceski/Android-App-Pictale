package pictale.mk

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log.d
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_details.*
import kotlinx.android.synthetic.main.activity_startup.*
import pictale.mk.events.APIv2
import pictale.mk.events.ResponseInsertFav
import pictale.mk.events.RetrofitInstanceV2
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
         val eventName = intent.getStringExtra("name")
         val eventLocation = intent.getStringExtra("location")
         val eventId = intent.getStringExtra("eventId")
        name_of_event.text = eventName
        location_of_event.text = eventLocation
        back_page_click.setOnClickListener {
            startActivity(Intent(this@DetailsActivity,HomeActivity::class.java))
        }
        btn_fav.setOnClickListener {
            btn_fav.setImageResource(R.drawable.ic_star_full)
            val sharedPreferences=getSharedPreferences("preferences",Context.MODE_PRIVATE)
            val token=sharedPreferences.getString("token","")
            val api=RetrofitInstanceV2.getRetrofitInstance().create(APIv2::class.java)
            api.insertEventFav("Bearer $token",eventId!!).enqueue(object :Callback<ResponseInsertFav>{
                override fun onResponse(
                    call: Call<ResponseInsertFav>,
                    response: Response<ResponseInsertFav>
                ) {
                    d("Good","Response")
                }

                override fun onFailure(call: Call<ResponseInsertFav>, t: Throwable) {
                    d("Failure","${t.message}")
                }

            })

        }

    }

}