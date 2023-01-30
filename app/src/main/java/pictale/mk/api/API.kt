package pictale.mk.api

import pictale.mk.model.Signin
import pictale.mk.model.Signup
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface API {
    @FormUrlEncoded
    @POST("auth/register/")
    fun createUser(
        @Field("email") email:String,
        @Field("firstName") firstName:String,
        @Field("lastName") lastName:String,
        @Field("password") password:String
    ): Call<Signup>

    @FormUrlEncoded
    @POST("auth/login/")
    fun loginUser(
        @Field("email") email:String,
        @Field("password") password:String
    ): Call<Signin>

    @GET("auth/login/")
    fun getTokenLogin(
        @Field("token") email:String
    ): Call<SignInResponse>

    @GET("auth/register/")
    fun getToken(
        @Field("token") email:String
    ): Call<SignUpResponse>
}