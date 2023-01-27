package pictale.mk.api

import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

public interface API {
    @FormUrlEncoded
    @POST("/") //<--napravi promena
    Call<Signup> createUser(

    );

}