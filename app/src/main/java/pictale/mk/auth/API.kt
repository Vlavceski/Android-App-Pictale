package pictale.mk.auth

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


}