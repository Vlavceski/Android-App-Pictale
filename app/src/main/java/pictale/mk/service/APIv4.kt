package pictale.mk.service

import pictale.mk.access.ResponseAccessInEvent
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface APIv4 {
    @GET("private/firebase/list-logged-user-notifications")
    fun sendNotification(
        @Header("Authorization") token: String?
    ): Call<List<FcmResponse>>

}