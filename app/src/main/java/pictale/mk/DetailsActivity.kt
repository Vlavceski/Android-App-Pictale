package pictale.mk

import android.*
import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log.d
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_add_event.*
import kotlinx.android.synthetic.main.activity_details.*
import kotlinx.android.synthetic.main.activity_details.back_page_click
import kotlinx.android.synthetic.main.activity_setting.*
import kotlinx.android.synthetic.main.activity_splash_screen.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
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
    var pickedPhoto: Uri? = null
    var pickedBitMap: Bitmap? = null
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
            pickedPhoto(it)
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

    fun pickedPhoto (view: View) {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 2)

//        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
//            != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)
//        } else {
//            val galleryIntext = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//            startActivityForResult(galleryIntext, 2)
//        }
    }
    //Upload Image from Gallery
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray ) {
        if(grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            val galleryIntext = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryIntext, 2)
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
    //Upload Image from Gallery
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            val selectedPhotos = mutableListOf<Uri>()
            val clipData = data?.clipData

            if (clipData != null) {
                for (i in 0 until clipData.itemCount) {
                    selectedPhotos.add(clipData.getItemAt(i).uri)
                }
            } else {
                var s=data?.data?.let { selectedPhotos.add(it) }

                d("in else","$s")
            }

            d("sel","$selectedPhotos")
            uploadFiles(selectedPhotos)

        }


//        if(requestCode == 2 && resultCode == RESULT_OK && data != null){
//            pickedPhoto = data.data
//            if(Build.VERSION.SDK_INT >= 28){
//                val source = ImageDecoder.createSource(this.contentResolver, pickedPhoto!!)
//                pickedBitMap = ImageDecoder.decodeBitmap(source)
//                profile_picture.setImageBitmap(pickedBitMap)
//            } else {
//                pickedBitMap = MediaStore.Images.Media.getBitmap(this.contentResolver, pickedPhoto)
//                profile_picture.setImageBitmap(pickedBitMap)
//            }
//
//            val eventId = intent.getStringExtra("eventId")
//            val file = File(getRealPathFromURI(pickedPhoto!!))
//            val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
//            val fileToUpload = MultipartBody.Part.createFormData("file", file.name, requestFile)
//            d("----","$fileToUpload")
//
//            val sharedPreferences = getSharedPreferences("preferences", Context.MODE_PRIVATE)
//            val token = sharedPreferences.getString("token", "")
//            val api = RetrofitInstance.getRetrofitInstance().create(API::class.java)
//
//            api.updatePicture("Bearer $token",fileToUpload)
//                .enqueue(object :Callback<ResponseUploadPicture>{
//                    override fun onResponse(
//                        call: Call<ResponseUploadPicture>,
//                        response: Response<ResponseUploadPicture>
//                    ) {
//                        d("ispratena","slika")
//                    }
//
//                    override fun onFailure(call: Call<ResponseUploadPicture>, t: Throwable) {
//                        d("Failure","----------------------")
//                    }
//                })
//        }




        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun uploadFiles(selectedPhotos: MutableList<Uri>) {
        val files = mutableListOf<File>()
        val paths = getRealPathsFromURIs(selectedPhotos)
        for (path in paths) {
            val file = File(path)
            files.add(file)
        }

        val request= MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .apply {
                for (file in files) {
                    addFormDataPart("file[]", file.name, file.asRequestBody("image/*".toMediaTypeOrNull()))
                }
            }
            .build()
        val listImages= listOf<MultipartBody>(request)
        d("Nesto","$listImages")

        val sharedPreferences = getSharedPreferences("preferences", Context.MODE_PRIVATE)
            val token = sharedPreferences.getString("token", "")
            val api = RetrofitInstanceV2.getRetrofitInstance().create(APIv2::class.java)
            val eventId = intent.getStringExtra("eventId")

            api.uploadFiles("Bearer $token",eventId, listImages).enqueue(object :Callback<ResponseUploadFile>{
                override fun onResponse(
                    call: Call<ResponseUploadFile>,
                    response: Response<ResponseUploadFile>
                ) {
                    d("Good","Success")
                }

                override fun onFailure(call: Call<ResponseUploadFile>, t: Throwable) {
                    TODO("Not yet implemented")
                }
            })


    }



    @SuppressLint("Range")
    fun getRealPathsFromURIs(uris: MutableList<Uri>): List<String> {
        val paths = mutableListOf<String>()
        for (uri in uris) {
            val cursor = contentResolver.query(uri, null, null, null, null)
            cursor?.moveToFirst()
            val path = cursor?.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
            cursor?.close()
            path?.let { paths.add(it) }
        }
        return paths
    }


}



