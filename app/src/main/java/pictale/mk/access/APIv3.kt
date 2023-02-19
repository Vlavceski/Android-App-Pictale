package pictale.mk.access

import pictale.mk.events.EventUser
import pictale.mk.events.ResponseDetails
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface APIv3 {

    @GET("private/event/user/list-all-users-for-access-in-event")
    fun getListAllUsersForAccessInEvent(
        @Query("eventId") eventId: String): Call<List<ResponseListAll>>


    @POST("private/event/request-user-access-in-event")
    fun accessInEvent(
        @Query("eventId") eventId: String,
        @Query("userId") user: String
    ):Call<ResponseAccessInEvent>

//    @FormUrlEncoded
    @POST("private/event/user/approve-user-access-in-event")
    fun approveAccessInEvent(
        @Query("eventId") eventId: String,
        @Query("userCollaboration") userCollaboration:String,
        @Query("userId") userId:String
    ):Call<ResponseApproveAccessInEvent>

    @POST("private/event/user/invite-user-for-event")
    fun inviteUserForEvent(
        @Query("eventId") eventId: String,
        @Query("userId") userId: String
    ):Call<ResponseInviteUserForEvent>

    @DELETE("private/event/user/reject-access-in-event")
    fun rejectAccessInEvent(
        @Query("eventId") eventId: String,
        @Query("userId") userId: String
    ):Call<ResponseReject>


//    @POST("")
//    fun setUserCollaborationInEvent(
//        //body
//    ):Call<ResponseSetUserCollaboration>

}