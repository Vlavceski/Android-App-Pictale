package pictale.mk.auth

import okhttp3.MultipartBody
import pictale.mk.auth.responses.*
import retrofit2.Call
import retrofit2.http.*

interface API {

    @Headers("Content-Type:application/json")
    @POST("auth/register")
    fun signup(@Body signup: Signup): Call<ResponseBody>

    @Headers("Content-Type:application/json")
    @POST("auth/login")
    fun signin(@Body signin: Signin): Call<TokenResponse>

    @GET("user/getLoggedUser")
    fun getClient(@Header("Authorization") token: String?): Call<LoggedResponse>

    @Headers("Content-Type:application/json")
    @POST("user/updateUserInfo")
    fun updateInfo(@Header("Authorization") token: String?, @Body updateInfo: UpdateInfo): Call<ResponseUpdateInfo>

    @Headers("Content-Type:application/json")
    @POST("user/updatePassword")
    fun updatePassword(@Header("Authorization") token: String?, @Body updatePassword: UpdatePassword): Call<ResponseUpdatePassword>

    @Multipart
    @POST("user/updateProfilePicture")
    fun updatePicture(@Header("Authorization") token: String?, @Part file: MultipartBody.Part): Call<ResponseUploadPicture>

    @GET("user/list-favourite-events")
    fun getFavList(@Header("Authorization") token: String?): Call<List<ResponseFav>>

    @Headers("Content-Type:application/json")
    @POST("auth/googleLogin")
    fun signinWithGoogle(@Body googleSignIn: GoogleSignIn): Call<ResponseGoogleLogin>


}