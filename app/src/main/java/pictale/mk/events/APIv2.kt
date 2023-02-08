package pictale.mk.events

import pictale.mk.auth.responses.ResponseAllEvents
import retrofit2.Call
import retrofit2.http.*

interface APIv2 {

    @GET("event/findEventsByParameter")
    fun getPublicEvents(@Query("value") public: String): Call<List<ResponseAllEvents>>

    @POST("event/createNewEvent")
    fun addEvent(@Header("Authorization") token: String?,@Body addEventBody: AddEventBody):Call<ResponseAddEvent>

    @GET("event/search/name-location-description")
    fun searchItem(@Query("text") text: String):Call<List<ResponseAllEvents>>


}