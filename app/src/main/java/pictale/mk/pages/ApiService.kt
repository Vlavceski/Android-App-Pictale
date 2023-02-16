package pictale.mk.pages

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    //APi for pages
    @GET(Constants.END_POINT)
    fun getPages(
        @Query("eventPublicityType")  eventPublicityType :String?,
        @Query("page")  page:String?,
        @Query("size")  size:String?,
    ): Response<ResponsePages>
}