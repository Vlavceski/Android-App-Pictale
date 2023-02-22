package pictale.mk.access

import retrofit2.Call
import retrofit2.http.*

interface APIv3 {

    @GET("private/event/user/list-all-users-for-access-in-event")
    fun getListAllUsersForAccessInEvent(
        @Header("Authorization") token: String?,
        @Query("eventId") eventId: String): Call<List<ResponseListAll>>

    @GET("/web/services/private/event/user/list-all-approved-users")
    fun getListAllUsersWithPermissions(
        @Header("Authorization") token: String?,
        @Query("eventId") eventId: String): Call<List<ResponseUsersWithPermissions>>

    @POST("private/event/request-user-access-in-event")
    fun accessInEvent(
        @Query("eventId") eventId: String,
        @Query("userId") user: String
    ):Call<ResponseAccessInEvent>

//    @FormUrlEncoded
    @POST("private/event/user/approve-user-access-in-event")
    fun approveAccessInEvent(
        @Header("Authorization") token: String?,
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
        @Header("Authorization") token: String?,
        @Query("eventId") eventId: String,
        @Query("userId") userId: String
    ):Call<ResponseReject>


    @DELETE("private/event/user/remove-user-from-event")
    fun removeUserFromEvent(
        @Header("Authorization") token: String?,
        @Query("eventId") eventId: String,
        @Query("userId") userId: String
    ):Call<ResponseRemoveUser>


//    @POST("")
//    fun setUserCollaborationInEvent(
//        //body
//    ):Call<ResponseSetUserCollaboration>

}