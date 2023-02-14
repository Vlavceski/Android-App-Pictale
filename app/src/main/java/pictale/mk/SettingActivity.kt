package pictale.mk

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
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
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import kotlinx.android.synthetic.main.activity_details.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_setting.*
import kotlinx.android.synthetic.main.activity_setting.back_page_click
import kotlinx.android.synthetic.main.activity_setting.view.*
import kotlinx.android.synthetic.main.activity_splash_screen.*
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import pictale.mk.auth.*
import pictale.mk.auth.responses.LoggedResponse
import pictale.mk.auth.responses.ResponseUploadPicture
import pictale.mk.events.APIv2
import pictale.mk.events.ResponseUploadFile
import pictale.mk.events.RetrofitInstanceV2
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class SettingActivity : AppCompatActivity() {
    private lateinit var filePath: String
    var pickedPhoto: Uri? = null
    var pickedBitMap: Bitmap? = null
    @SuppressLint("InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        select_image.setOnClickListener {
            pickedPhoto(it)
        }
//        upload_image.setOnClickListener {
//            uploadImage()
//        }

        back_page_click.setOnClickListener {
            startActivity(Intent(this,HomeActivity::class.java))
        }
        btn_change.setOnClickListener {
            startActivity(Intent(this,ChangeInfoActivity::class.java))
        }

        btn_change_password.setOnClickListener {
            startActivity(Intent(this,ChangePasswordActivity::class.java))

        }
    }




    private fun uploadImage() {
        val file = File(filePath)
        val requestBody = RequestBody.create("image/*".toMediaTypeOrNull(), file)
        val image = MultipartBody.Part.createFormData("image", file.name, requestBody)


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
                profile_picture.setImageBitmap(pickedBitMap)
            } else {
                pickedBitMap = MediaStore.Images.Media.getBitmap(this.contentResolver, pickedPhoto)
                profile_picture.setImageBitmap(pickedBitMap)
            }
            val file = File(getRealPathFromURI(pickedPhoto!!))
            val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
            val fileToUpload = MultipartBody.Part.createFormData("file", file.name, requestFile)
            d("----","$fileToUpload")

            val sharedPreferences = getSharedPreferences("preferences", Context.MODE_PRIVATE)
            val token = sharedPreferences.getString("token", "")
            val api = RetrofitInstance.getRetrofitInstance().create(API::class.java)

            api.updatePicture("Bearer $token",fileToUpload)
                .enqueue(object :Callback<ResponseUploadPicture>{
                    override fun onResponse(
                        call: Call<ResponseUploadPicture>,
                        response: Response<ResponseUploadPicture>
                    ) {
                        d("ispratena","slika")
                    }

                    override fun onFailure(call: Call<ResponseUploadPicture>, t: Throwable) {
                        d("Failure","----------------------")
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





    override fun onStart() {
        super.onStart()
        val sharedPreferences = getSharedPreferences("preferences", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("token", "")
        d("Token>>>","$token")
        updateUI()

    }

    private fun updateUI() {
        val sharedPreferences = getSharedPreferences("preferences", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("token", "")
        d("Tokenot od home","$token")
        if (token==""){
            startActivity(Intent(this,LoginActivity::class.java))
        }
        showProfile()

    }

    private fun showProfile() {
        val sharedPreferences = getSharedPreferences("preferences", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("token", "")
        val api = RetrofitInstance.getRetrofitInstance().create(API::class.java)

        d("Profile_token-->", "$token")

        api.getClient("Bearer $token").enqueue(object :Callback<LoggedResponse>{
            override fun onResponse(call: Call<LoggedResponse>, response: Response<LoggedResponse>) {
                d("Profile_response-->", "${response.body()}")
                if (response.code()==200) {

                    d("in Response-->", response.body()?.email.toString())
                    val email=response.body()?.email.toString()
                    val firstName=response.body()?.firstName.toString()
                    val lastName=response.body()?.lastName.toString()
                    val picture=response.body()?.pictureUrl
                    d("picureRespones-","$picture")

                    Glide.with(this@SettingActivity)
                        .load(picture)
                        .transform(CircleCrop())
                        .into(profile_picture)
                    your_email.setText("Email: $email")
                    your_first_name.setText("First Name: $firstName")
                    your_last_name.setText("Last Name: $lastName")

                }
            }

            override fun onFailure(call: Call<LoggedResponse>, t: Throwable) {
                Toast.makeText(this@SettingActivity, t.message, Toast.LENGTH_SHORT)
                    .show()
                t.message?.let { Log.d("Login_failure-->", it) }

            }
        })
    }


}