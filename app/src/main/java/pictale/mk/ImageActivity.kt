package pictale.mk

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import kotlinx.android.synthetic.main.activity_image.*
import kotlinx.android.synthetic.main.item_event_layout.view.*
import java.io.*
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.concurrent.Executors


@Suppress("CAST_NEVER_SUCCEEDS")
class ImageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)
//        val image = intent.getSerializableExtra("imageUrisString") as Uri
//        d("image--","$image")

        val extras = intent.extras
        val myUri = Uri.parse(extras!!.getString("imageUri"))
//        iv_photo.setImageURI(myUri)
        Glide.with(this)
            .load(myUri)
            .transform(RoundedCorners(30))
            .into(display_image)

        val myExecutor = Executors.newSingleThreadExecutor()
        val myHandler = Handler(Looper.getMainLooper())
        btn_download.setOnClickListener {
            myExecutor.execute {
                val mImage = mLoad(myUri.toString())
                myHandler.post {
                    display_image.setImageBitmap(mImage)
                    if(mImage!=null){
                        mSaveMediaToStorage(mImage)
                    }
                }
            }
        }
        back_page_click.setOnClickListener {
            startActivity(Intent(this,DetailsActivity::class.java))
        }
    }

    private fun mLoad(string: String): Bitmap? {

        val url: URL = mStringToURL(string)!!
        val connection: HttpURLConnection?
        try {
            connection = url.openConnection() as HttpURLConnection
            connection.connect()
            val inputStream: InputStream = connection.inputStream
            val bufferedInputStream = BufferedInputStream(inputStream)
            return BitmapFactory.decodeStream(bufferedInputStream)
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(applicationContext, "Error", Toast.LENGTH_LONG).show()
        }
        return null

    }

    private fun mStringToURL(string: String): URL? {
        try {
            return URL(string)
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        }
        return null
    }
    private fun mSaveMediaToStorage(bitmap: Bitmap?) {
        val filename = "${System.currentTimeMillis()}.jpg"
        var fos: OutputStream? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            this.contentResolver?.also { resolver ->
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                }
                val imageUri: Uri? = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                fos = imageUri?.let { resolver.openOutputStream(it) }
            }
        } else {
            val imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val image = File(imagesDir, filename)
            fos = FileOutputStream(image)
        }
        fos?.use {
            bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, it)
            Toast.makeText(this , "Saved to Gallery" , Toast.LENGTH_SHORT).show()
        }
    }
}