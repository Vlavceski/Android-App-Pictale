package pictale.mk

import android.*
import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.util.Log.d
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
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
import okhttp3.RequestBody
import pictale.mk.access.APIv3
import pictale.mk.access.ResponseAccessInEvent
import pictale.mk.access.ResponseListAll
import pictale.mk.access.RetrofitInstanceV3
import pictale.mk.adapters.ImageAdapter
import pictale.mk.auth.API
import pictale.mk.auth.AuthToken
import pictale.mk.auth.RetrofitInstance
import pictale.mk.auth.responses.LoggedResponse
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
        rc_view.adapter = ImageAdapter(this@DetailsActivity, imageUrisString,eventId)

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

        menu_icon.setOnClickListener {
            var creator=creator(eventId)
            val popupMenu = PopupMenu(this, menu_icon)
            popupMenu.inflate(R.menu.details_menu)
            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.add_fav -> {
                        addToFav()
                        true
                    }
                    R.id.update_thumbnail -> {
                        startActivity(Intent(this,UploadThumbnail::class.java))
                        true
                    }
                    R.id.people -> {
                        getUsers(eventId)
                        true
                    }
                    R.id.request -> {
                        makeRequest(eventId)
                        true
                    }

                    R.id.delete_event -> {
                        true
                    }
                    else -> false
                }
            }
            popupMenu.show()
        }


    }

    private fun creator(eventId: String?): Boolean {
    //napisi fun za creator
    }

    private fun makeRequest(eventId: String?) {
            val token= AuthToken.get(this@DetailsActivity)
            val api= RetrofitInstance.getRetrofitInstance().create(API::class.java)
            api.getClient("Bearer $token").enqueue(object :Callback<LoggedResponse>{
                override fun onResponse(call: Call<LoggedResponse>, response: Response<LoggedResponse>) {
                    if (response.code()==200) {
                        var userId=response.body()?.id
                        accessWithIds(userId,eventId!!)
                        d("response-send","user- $userId, event- $eventId")
                    }
                }

                override fun onFailure(call: Call<LoggedResponse>, t: Throwable) {
                    t.message?.let { Log.d("Login_failure-->", it) }
                }
            })

        }

        private fun accessWithIds(userId: String?, eventId: String) {
            val api=RetrofitInstanceV3.getRetrofitInstance().create(APIv3::class.java)
            api.accessInEvent(eventId,userId!!).enqueue(object :Callback<ResponseAccessInEvent>{
                override fun onResponse(
                    call: Call<ResponseAccessInEvent>,
                    response: Response<ResponseAccessInEvent>
                ) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@DetailsActivity, "${response.body()}", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@DetailsActivity, "${response.errorBody()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ResponseAccessInEvent>, t: Throwable) {
                    d("Failure","${t.message}")
                }

            })
        }




    private fun getUsers(eventId: String?) {
        val api=RetrofitInstanceV3.getRetrofitInstance().create(APIv3::class.java)
        api.getListAllUsersForAccessInEvent(eventId!!)
            .enqueue(object : Callback<List<ResponseListAll>>{
                override fun onResponse(
                    call: Call<List<ResponseListAll>>,
                    response: Response<List<ResponseListAll>>
                ) {
                    val res=response.body()
                    d("response-users","${response.body()}")
                    openDialog(res)
                }

                override fun onFailure(call: Call<List<ResponseListAll>>, t: Throwable) {
                    d("failure","")
                }
            })

    }

    private fun openDialog(res: List<ResponseListAll>?) {
        val personNames = res?.map { "${it.firstName} ${it.lastName}" }?.toTypedArray()

        val builder = AlertDialog.Builder(this@DetailsActivity)
//        builder.setTitle("Dialog Title")
//        builder.setMessage("Dialog message goes here.")
        if (res==null){
            builder.setTitle("Sorry, there is none waiting to be approved in this event yet!")
        }
        builder.setItems(personNames) { _, position ->
            // Handle selection of a person
            val selectedPerson = res?.get(position)
            // Do something with the selected person, such as displaying their details
        }
        builder.setPositiveButton("OK") { dialog, which ->
            // do something when OK button is clicked
        }
//        builder.setNegativeButton("Cancel") { dialog, which ->
//            // do something when Cancel button is clicked
//        }
        val dialog = builder.create()
        dialog.show()
    }


    private fun addToFav(){
    val sharedPreferences = getSharedPreferences("preferences", Context.MODE_PRIVATE)
    val token = sharedPreferences.getString("token", "")

    val eventId = intent.getStringExtra("eventId")
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


    fun pickedPhoto (view: View) {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)
        } else {
            val galleryIntext = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryIntext, 2)
        }
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
        if(requestCode == 2 && resultCode == RESULT_OK && data != null){
            pickedPhoto = data.data
            if(Build.VERSION.SDK_INT >= 28){
                val source = ImageDecoder.createSource(this.contentResolver, pickedPhoto!!)
                pickedBitMap = ImageDecoder.decodeBitmap(source)
//                profile_picture.setImageBitmap(pickedBitMap)
            } else {
                pickedBitMap = MediaStore.Images.Media.getBitmap(this.contentResolver, pickedPhoto)
//                profile_picture.setImageBitmap(pickedBitMap)
            }
            val file = File(getRealPathFromURI(pickedPhoto!!))
            val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
            val fileToUpload = MultipartBody.Part.createFormData("file", file.name, requestFile)
            d("----","$fileToUpload")

            val sharedPreferences = getSharedPreferences("preferences", Context.MODE_PRIVATE)
            val token = sharedPreferences.getString("token", "")
            val api = RetrofitInstanceV2.getRetrofitInstance().create(APIv2::class.java)
            d("photo-->","$fileToUpload")

            val eventId = intent.getStringExtra("eventId")
            api.uploadFiles("Bearer $token",eventId, fileToUpload)
                .enqueue(object :Callback<ResponseUploadFile>{
                    override fun onResponse(
                        call: Call<ResponseUploadFile>,
                        response: Response<ResponseUploadFile>
                    ) {
                        d("-------","Success")
                    }

                    override fun onFailure(call: Call<ResponseUploadFile>, t: Throwable) {
                    d("-----","${t.message}")
                    }

                })


        }
        super.onActivityResult(requestCode, resultCode, data)
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



}




