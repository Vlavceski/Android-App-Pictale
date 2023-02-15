package pictale.mk

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_startup.*
import kotlinx.android.synthetic.main.activity_upload_thumbnail.*

class UploadThumbnail : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_thumbnail)
        close_window.setOnClickListener {
            startActivity(Intent(this,DetailsActivity::class.java))
        }
        update_thumbnail.setOnClickListener {

        }

    }
}