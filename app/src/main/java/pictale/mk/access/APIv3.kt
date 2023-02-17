package pictale.mk.access

import pictale.mk.events.ResponseDetails
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface APIv3 {

    @GET("private/event/user/list-all-users-for-access-in-event")
    fun getListAllUsersForAccessInEvent(
        @Query("eventId") eventId: String): Call<List<ResponseListAll>>



}