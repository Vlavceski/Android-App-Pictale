package pictale.mk.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import pictale.mk.auth.ResponseBody
import pictale.mk.auth.Signin
import pictale.mk.auth.Signup
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ApiInterface {
    @Headers("Content-Type:application/json")
    @POST("auth_tokens")
    fun signin(@Body info:Signin
    ): retrofit2.Call<ResponseBody>

    @Headers("Content-Type:application/json")
    @POST("/api/v1/auth/register/")
    fun registerUser(
        @Body info: Signup
    ): retrofit2.Call<ResponseBody>


}

class RetrofitInstance {
    companion object {
        val BASE_URL: String = "http://88.85.111.72:37990"

        val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        }

        val client: OkHttpClient = OkHttpClient.Builder().apply {
            this.addInterceptor(interceptor)
        }.build()
        fun getRetrofitInstance(): Retrofit {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }
}