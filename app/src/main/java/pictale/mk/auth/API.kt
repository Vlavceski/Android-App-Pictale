package pictale.mk.auth

import retrofit2.Call
import retrofit2.http.*

interface API {
    @Headers("Content-Type:application/json")
    @POST("auth/register")
    fun signup(@Body signup: Signup): Call<ResponseBody>


//    @Headers("Content-Type:application/json")
    @POST("auth/login")
    fun signin(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<TokenResponse>

    @GET("user/getLoggedUser")
    fun getClient(@Header("Authorization") bearerToken: String?): Call<LoggedResponse>
//
//    @GET("user/getLoggedUser")
//    fun getClient(): Call<LoggedResponse>


}