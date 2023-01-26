package pictale.mk.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class EventsApiClient {

    companion object {
        private var eventsApi: EventsApi? = null

        fun getEventApi(): EventsApi? {

            if (eventsApi == null) {
                eventsApi = Retrofit.Builder()
                    .baseUrl("")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(EventsApi::class.java)

            }

            return eventsApi
        }

    }
}