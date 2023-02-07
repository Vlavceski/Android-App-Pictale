package pictale.mk.events

import pictale.mk.auth.responses.ResponseAllEvents
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface APIv2 {

    @GET("event/findEventsByParameter")
    fun getPublicEvents(@Query("value") public: String): Call<List<ResponseAllEvents>>






}