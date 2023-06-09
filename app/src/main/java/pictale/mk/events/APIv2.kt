package pictale.mk.events

import okhttp3.MultipartBody
import pictale.mk.auth.responses.ResponseAllEvents
import retrofit2.Call
import retrofit2.http.*

interface APIv2 {

    @GET("event/findEventsByParameter")
    fun getPublicEvents(@Query("value") public: String): Call<List<ResponseAllEvents>>

    @DELETE("event/delete-file-from-event")
    fun deleteFileFromEvent(@Header("Authorization") token: String?,
                            @Query("eventFileId") eventFileId: String,
                            @Query("eventId") eventId: String):Call<ResponseDeleteFile>

    @POST("event/createNewEvent")
    fun addEvent(@Header("Authorization") token: String?,@Body addEventBody: AddEventBody):Call<ResponseAddEvent>

    @GET("event/search/name-location-description")
    fun searchItem(@Query("text") text: String):Call<List<ResponseAllEvents>>

    @GET("event/id/{id}")
    fun getDetails(@Path("id") id: String): Call<ResponseDetails>

    @POST("event/insert-event-into-favourites")
    fun insertEventFav(@Header("Authorization") token: String?,@Query("eventId") eventId: String):Call<ResponseInsertFav>

    @DELETE("event/remove-event-from-favourites")
    fun deleteEventFav(@Header("Authorization") token: String?,@Query("eventId") eventId: String):Call<ResponseDeleteFav>

    @Multipart
    @POST("event/upload-multiple-files")
    fun uploadFiles(
        @Header("Authorization") token: String?,
        @Query("eventId")  eventId:String?,
        @Part file: MultipartBody.Part
    ): Call<ResponseUploadFile>

    @Multipart
    @POST("event/upload-event-thumbnail")
    fun uploadThumbnail(
        @Header("Authorization") token: String?,
        @Query("eventId")  eventId:String?,
        @Part file: MultipartBody.Part
    ): Call<ResponseUpdateThumbnail>


    //APi for pages
    @GET("event/pageable/findAllByParameter")
    fun getPages(
        @Query("eventPublicityType") eventPublicityType: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): Call<ResponseAllEventsPages>

}