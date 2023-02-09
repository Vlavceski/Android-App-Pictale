package pictale.mk

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log.d
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_details.*
import kotlinx.android.synthetic.main.activity_startup.*
import pictale.mk.events.APIv2
import pictale.mk.events.RetrofitInstanceV2

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


    }

}