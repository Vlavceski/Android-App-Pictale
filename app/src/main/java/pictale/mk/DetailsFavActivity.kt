package pictale.mk

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues
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
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import kotlinx.android.synthetic.main.activity_details.*
import kotlinx.android.synthetic.main.activity_details.back_page_click
import kotlinx.android.synthetic.main.activity_details.location_of_event
import kotlinx.android.synthetic.main.activity_details.name_of_event
import kotlinx.android.synthetic.main.activity_details_fav.*
import kotlinx.android.synthetic.main.activity_details_fav.add_file
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import pictale.mk.access.*
import pictale.mk.adapters.*
import pictale.mk.auth.API
import pictale.mk.auth.AuthToken
import pictale.mk.auth.RetrofitInstance
import pictale.mk.auth.responses.LoggedResponse
import pictale.mk.events.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class DetailsFavActivity : AppCompatActivity() {
    var pickedPhoto: Uri? = null
    var pickedBitMap: Bitmap? = null
    @Suppress("UNCHECKED_CAST")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details_fav)
        val eventName = intent.getStringExtra("name")
        val eventLocation = intent.getStringExtra("location")
        val eventId = intent.getStringExtra("eventId")
        val imageUrisString = intent.getSerializableExtra("imageUrisString") as List<Uri>
        d("ovde","$imageUrisString")

        rc_view_fav.layoutManager = GridLayoutManager(this@DetailsFavActivity,2)
        rc_view_fav.adapter = ImageAdapter(this@DetailsFavActivity, imageUrisString,eventId,eventName,eventLocation)

        //////////////////////////
        var userId=""
        val token= AuthToken.get(this@DetailsFavActivity)
        val api= RetrofitInstance.getRetrofitInstance().create(API::class.java)
        api.getClient("Bearer $token").enqueue(object :Callback<LoggedResponse>{
            override fun onResponse(call: Call<LoggedResponse>, response: Response<LoggedResponse>) {
                if (response.code()==200) {
                    userId= response.body()?.id.toString()
                    var responseE:List<ResponseUsersWithPermissions>
                    val api1= RetrofitInstanceV3.getRetrofitInstance().create(APIv3::class.java)
                    api1.getListAllUsersWithPermissions("Bearer $token",eventId!!)
                        .enqueue(object : Callback<List<ResponseUsersWithPermissions>>{
                            override fun onResponse(
                                call: Call<List<ResponseUsersWithPermissions>>,
                                response: Response<List<ResponseUsersWithPermissions>>
                            ) {
                                if(response.code()==200) {
                                    responseE = response.body()!!
                                    var checkColl=false
                                    responseE.forEach{
                                            user->
                                        if (user.id.toString()==userId.toString()){
                                            checkColl=true
                                        }
                                    }

                                    menu_icon_fav.setOnClickListener {
                                        d("moze vlez","$checkColl")
                                        if (checkColl){
                                            val popupMenu = PopupMenu(this@DetailsFavActivity, menu_icon_fav)
                                            popupMenu.inflate(R.menu.details_menu_fav)
                                            popupMenu.setOnMenuItemClickListener { menuItem ->
                                                when (menuItem.itemId) {
                                                    R.id.return_start -> {
                                                        returnStart(imageUrisString,eventId,eventName,eventLocation)
                                                        true
                                                    }
                                                    R.id.remove_event -> {
                                                        removeEventFromFav(eventId)
                                                        true
                                                    }
                                                    R.id.update_thumbnail -> {
                                                        startActivity(Intent(this@DetailsFavActivity,UploadThumbnail::class.java))
                                                        true
                                                    }
                                                    R.id.users -> {
                                                        getAllUsersWithPermissions(eventId)
                                                        true
                                                    }
                                                    R.id.usersForApprove -> {
                                                        getUsersForApprove(eventId,eventName,eventLocation,imageUrisString)
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
                                        else{
                                            val popupMenu = PopupMenu(this@DetailsFavActivity, menu_icon_fav)
                                            popupMenu.inflate(R.menu.own_menu_fav)
                                            popupMenu.setOnMenuItemClickListener { menuItem ->
                                                when (menuItem.itemId) {
                                                    R.id.remove_event -> {
                                                        removeEventFromFav(eventId)
                                                        true
                                                    }
                                                    R.id.request -> {
                                                        makeRequest(eventId)
                                                        true
                                                    }
                                                    else -> false
                                                }
                                            }
                                            popupMenu.show()
                                        }
                                    }


                                }
                                else{
                                    d("response-error", "${response.errorBody()}")
                                }
                            }

                            override fun onFailure(
                                call: Call<List<ResponseUsersWithPermissions>>,
                                t: Throwable
                            ) {
                                d("failure","${t.message}")
                            }
                        })

                }
                else{
                    Toast.makeText(this@DetailsFavActivity, "${response.errorBody()}", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<LoggedResponse>, t: Throwable) {
                t.message?.let { Log.d("Login_failure-->", it) }
            }
        })

        ///////////////////////////////////////////////////////////////
        btn_delete_event.setOnClickListener {
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

        add_file.setOnClickListener {
            pickedPhoto(it)
        }
        eventClicker_.setOnClickListener {
            startActivity(Intent(this@DetailsFavActivity,DetailsFavActivity::class.java))
        }


        name_of_event.text = eventName
        location_of_event.text = eventLocation
        back_page_click.setOnClickListener {
            startActivity(Intent(this@DetailsFavActivity, HomeActivity::class.java))
        }
        btn_delete_event.setOnClickListener {
            val sharedPreferences = getSharedPreferences("preferences", Context.MODE_PRIVATE)
            val token = sharedPreferences.getString("token", "")
            val api = RetrofitInstanceV2.getRetrofitInstance().create(APIv2::class.java)
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

    private fun removeEventFromFav(eventId: String) {
        val token=AuthToken.get(this)
        val api= RetrofitInstanceV2.getRetrofitInstance().create(APIv2::class.java)

        api.deleteEventFav("Bearer $token",eventId).enqueue(object :Callback<ResponseDeleteFav>{
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


    private fun returnStart(
        imageUrisString: List<Uri>,
        eventId: String,
        eventName: String?,
        eventLocation: String?
    ) {
        rc_view_fav.layoutManager = GridLayoutManager(this@DetailsFavActivity,2)
        rc_view_fav.adapter = ImageAdapterFav(this@DetailsFavActivity, imageUrisString,eventId,eventName,eventLocation)
    }


    private fun makeRequest(eventId: String?) {
        val token= AuthToken.get(this@DetailsFavActivity)
        val api= RetrofitInstance.getRetrofitInstance().create(API::class.java)
        api.getClient("Bearer $token").enqueue(object :Callback<LoggedResponse>{
            override fun onResponse(call: Call<LoggedResponse>, response: Response<LoggedResponse>) {
                if (response.code()==200) {
                    var userId=response.body()?.id
                    accessWithIds(userId,eventId!!)
                    d("response-send","user- $userId, event- $eventId")
                }
                else{
                    Toast.makeText(this@DetailsFavActivity, "${response.errorBody()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoggedResponse>, t: Throwable) {
                t.message?.let { Log.d("Login_failure-->", it) }
            }
        })

    }

    private fun accessWithIds(userId: String?, eventId: String) {
        val api= RetrofitInstanceV3.getRetrofitInstance().create(APIv3::class.java)
        api.accessInEvent(eventId,userId!!).enqueue(object :Callback<ResponseAccessInEvent>{
            override fun onResponse(
                call: Call<ResponseAccessInEvent>,
                response: Response<ResponseAccessInEvent>
            ) {
                if (response.isSuccessful) {

                    Firebase.messaging.subscribeToTopic("userRequestAccess")
                        .addOnCompleteListener { task ->
                            var msg = "Subscribed"
                            if (!task.isSuccessful) {
                                msg = "Subscribe failed"
                            }
                            Log.d(ContentValues.TAG, msg)
                            Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                        }
                    Toast.makeText(this@DetailsFavActivity, "${response.body()}", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@DetailsFavActivity, "${response.errorBody()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseAccessInEvent>, t: Throwable) {
                d("Failure","${t.message}")
            }

        })
    }

    private fun getAllUsersWithPermissions(eventId: String?) {
        val token=AuthToken.get(this)
        val api= RetrofitInstanceV3.getRetrofitInstance().create(APIv3::class.java)
        api.getListAllUsersWithPermissions("Bearer $token",eventId!!)
            .enqueue(object : Callback<List<ResponseUsersWithPermissions>>{
                override fun onResponse(
                    call: Call<List<ResponseUsersWithPermissions>>,
                    response: Response<List<ResponseUsersWithPermissions>>
                ) {
                    d("response-users","${response.body()}")
                    val apiData = response.body()
                    if (apiData != null) {
                        rc_view_fav.layoutManager = LinearLayoutManager(this@DetailsFavActivity)
                        rc_view_fav.adapter = UWPAdapter(this@DetailsFavActivity, apiData as MutableList<ResponseUsersWithPermissions>,eventId)
                        //UWP -Users with permissions
                    }
                }

                override fun onFailure(
                    call: Call<List<ResponseUsersWithPermissions>>,
                    t: Throwable
                ) {
                    d("failure","${t.message}")
                }
            })
    }


    private fun getUsersForApprove(
        eventId: String,
        eventName: String?,
        eventLocation: String?,
        imageUrisString: List<Uri>
    ) {
        val token=AuthToken.get(this)
        val api= RetrofitInstanceV3.getRetrofitInstance().create(APIv3::class.java)
        api.getListAllUsersForAccessInEvent("Bearer $token",eventId!!)
            .enqueue(object : Callback<List<ResponseListAll>>{
                @SuppressLint("ResourceType")
                override fun onResponse(
                    call: Call<List<ResponseListAll>>,
                    response: Response<List<ResponseListAll>>
                ) {

                    d("response-users","${response.body()}")
                    val apiData = response.body()
                    if (apiData != null) {
                        rc_view_fav.layoutManager = LinearLayoutManager(this@DetailsFavActivity)
                        rc_view_fav.adapter = ApproveAdapterFav(this@DetailsFavActivity,
                            apiData as MutableList<ResponseListAll>,eventId,eventName,eventLocation,imageUrisString)
                    }

                }

                override fun onFailure(call: Call<List<ResponseListAll>>, t: Throwable) {
                    d("failure","${t.message}")
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
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray ) {
        if(grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            val galleryIntext = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryIntext, 2)
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == 2 && resultCode == RESULT_OK && data != null){
            pickedPhoto = data.data
            if(Build.VERSION.SDK_INT >= 28){
                val source = ImageDecoder.createSource(this.contentResolver, pickedPhoto!!)
                pickedBitMap = ImageDecoder.decodeBitmap(source)
            } else {
                pickedBitMap = MediaStore.Images.Media.getBitmap(this.contentResolver, pickedPhoto)
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