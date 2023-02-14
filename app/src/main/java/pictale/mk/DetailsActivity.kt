package pictale.mk

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log.d
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_add_event.*
import kotlinx.android.synthetic.main.activity_details.*
import kotlinx.android.synthetic.main.activity_splash_screen.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import pictale.mk.adapters.ImageAdapter
import pictale.mk.events.APIv2
import pictale.mk.events.ResponseInsertFav
import pictale.mk.events.ResponseUploadFile
import pictale.mk.events.RetrofitInstanceV2
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class DetailsActivity : AppCompatActivity() {

    private var selectedImageUri: Uri? = null
    @Suppress("UNCHECKED_CAST")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        val eventName = intent.getStringExtra("name")
        val eventLocation = intent.getStringExtra("location")
        val eventId = intent.getStringExtra("eventId")
        val imageUrisString = intent.getSerializableExtra("imageUrisString") as List<Uri>

        rc_view.layoutManager = LinearLayoutManager(this@DetailsActivity)
        rc_view.adapter = ImageAdapter(this@DetailsActivity, imageUrisString)


        add_file.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(READ_EXTERNAL_STORAGE),
                    SELECT_IMAGE_PERMISSION_REQUEST_CODE
                )
            } else {
                selectImage()
            }
        }

        upload_file.setOnClickListener {
            if (selectedImageUri == null) {
                Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            uploadImage(eventId)
        }

        name_of_event.text = eventName
        location_of_event.text = eventLocation
        back_page_click.setOnClickListener {
            startActivity(Intent(this@DetailsActivity, HomeActivity::class.java))
        }
        btn_fav.setOnClickListener {
            btn_fav.setImageResource(R.drawable.ic_star_full)
            val sharedPreferences = getSharedPreferences("preferences", Context.MODE_PRIVATE)
            val token = sharedPreferences.getString("token", "")
            val api = RetrofitInstanceV2.getRetrofitInstance().create(APIv2::class.java)
            api.insertEventFav("Bearer $token", eventId!!)
                .enqueue(object : Callback<ResponseInsertFav> {
                    override fun onResponse(
                        call: Call<ResponseInsertFav>,
                        response: Response<ResponseInsertFav>
                    ) {
                        d("Good", "Response")
                    }

                    override fun onFailure(call: Call<ResponseInsertFav>, t: Throwable) {
                        d("Failure", "${t.message}")
                    }

                })

        }

    }

    private fun uploadImage(eventId: String?) {
        val image = selectedImageUri ?: return
        val imageBody = createImageBody(image)
        val sharedPreferences = getSharedPreferences("preferences", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("token", "")
        val api=RetrofitInstanceV2.getRetrofitInstance().create(APIv2::class.java)

        api.uploadFiles("Bearer $token",eventId!!,imageBody).enqueue(object :Callback<ResponseUploadFile>{
            override fun onResponse(
                call: Call<ResponseUploadFile>,
                response: Response<ResponseUploadFile>
            ) {
                d("Success","Good")
            }

            override fun onFailure(call: Call<ResponseUploadFile>, t: Throwable) {
                d("Failure","${t.message}")
            }

        })

    }

    private fun createImageBody(image: Uri): MultipartBody.Part {
        val file = File(getRealPathFromURI(image))
        val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
        return MultipartBody.Part.createFormData("image", file.name, requestFile)
    }

    private fun getRealPathFromURI(uri: Uri): String {
        val cursor = contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            it.moveToFirst()
            val idx = it.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            return it.getString(idx)
        }
        return ""
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == SELECT_IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            selectedImageUri = data?.data
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        d("-----","$requestCode----$permissions ---$grantResults")
        if (requestCode == SELECT_IMAGE_PERMISSION_REQUEST_CODE &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            selectImage()
        } else {
            Toast.makeText(this, "Permission to read external storage is required to select image", Toast.LENGTH_SHORT).show()
        }
    }


    private fun selectImage() {
        val intent = Intent().apply {
            type = "image/*"
            action = Intent.ACTION_GET_CONTENT
        }
        startActivityForResult(Intent.createChooser(intent, "Select Image"), SELECT_IMAGE_REQUEST_CODE)

    }

    companion object {
        private const val SELECT_IMAGE_REQUEST_CODE = 1
        private const val SELECT_IMAGE_PERMISSION_REQUEST_CODE = 2
    }
}



