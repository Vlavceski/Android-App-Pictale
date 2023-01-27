package pictale.mk.api

import pictale.mk.model.Signup
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

public interface API {
    @FormUrlEncoded
    @POST("/") //<--napravi promena
    fun createUser(
        @Field("email") email:String,
        @Field("firstName") firstName:String,
        @Field("lastName") lastName:String,
        @Field("password") password:String
    ): Call<Signup>

}