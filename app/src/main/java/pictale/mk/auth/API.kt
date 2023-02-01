package pictale.mk.auth

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface API {
    @Headers("Content-Type:application/json")
    @POST("auth/register")
    fun signup(@Body signup: Signup): Call<ResponseBody>


    @Headers("Content-Type:application/json")
    @POST("auth/login")
    fun signin(@Body signin: Signin): Call<TokenResponse>

}