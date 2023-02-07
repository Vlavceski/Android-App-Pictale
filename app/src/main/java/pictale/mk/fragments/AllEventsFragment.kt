package pictale.mk.fragments

import android.os.Bundle
import android.util.Log
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_all_events.*
import org.json.JSONException
import org.json.JSONObject
import pictale.mk.R
import pictale.mk.adapters.EventAdapter
import pictale.mk.auth.responses.ResponseAllEvents
import pictale.mk.events.APIv2
import pictale.mk.events.RetrofitInstanceV2
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@Suppress("CAST_NEVER_SUCCEEDS")
class AllEventsFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: EventAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_all_events, container, false)
    }


    override fun  onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState);

        fetchData()
    }
/*
    private fun parseResponse(response: List<ResponseAllEvents>?): Any {
        val dataList = mutableListOf<ResponseAllEvents>()

        try {
            val jsonObject = JSONObject(response)
            val dataArray = jsonObject.getJSONArray("data")
            for (i in 0 until dataArray.length()) {
                val data = dataArray.getJSONObject(i)
                val location = data.getString("location")
                val name = data.getString("name")
                val dataModel = ResponseAllEvents(location, name)
                dataList.add(dataModel)
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return dataList
    }

    private fun makeApiCall(): List<ResponseAllEvents>? {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://88.85.111.72:37990/api/v2/event/findEventsByParameter?value=")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(APIv2::class.java)

        val call = apiService.getPublic()
        val response = call.execute().body()
        return response
    }

//    private fun fetchData(){
//            val api = RetrofitInstanceV2.getRetrofitInstance().create(APIv2::class.java)
//            api.getPublicEvents("public").enqueue(object :Callback<List<ResponseAllEvents>>{
//                override fun onResponse(call: Call<List<ResponseAllEvents>>, response: Response<List<ResponseAllEvents>>) {
//                    d("Allevents_response-->", "${response.body()}")
//                    if (response.code()==200) {
//                        d("in Response-->", "${response.body()}")
//                        val json=response.body().toString()
//                        /*
////                        try {
////                            val myUsersJSONFile : List<ResponseAllEvents>? = response.body()
////                            val size = myUsersJSONFile?.count()
////                            val buffer = size?.let { ByteArray(it) }
////                            myUsersJSONFile?.forEachIndexed(buffer)
////                            myUsersJSONFile.close()
////                            json = String(buffer, charset("UTF-8"))
////                        } catch (ex: IOException) {
////                            ex.printStackTrace()
////                            return null
////                        }
//                         */
//
//                        d("json-->", "${json}")
//
//
//                        val list: List<ResponseAllEvents> =
//                            Gson().fromJson(json.toString(), object : TypeToken<List<ResponseAllEvents?>?>() {}.type)
//
//
//                        d("json-->","$list")
//
//                        rvEventsList.layoutManager = LinearLayoutManager(activity)
//                        val itemAdapter = EventAdapter(requireActivity().applicationContext,
//                            list as MutableList<ResponseAllEvents>
//                        )
//                        rvEventsList.adapter = itemAdapter
//
//                    }
//                }
//                override fun onFailure(call: Call<List<ResponseAllEvents>>, t: Throwable) {
//                    t.message?.let { Log.d("Login_failure-->", it) }
//                }
//            })
//
//    }


 */

    private fun fetchData() {

        val api = RetrofitInstanceV2.getRetrofitInstance().create(APIv2::class.java)
        api.getPublicEvents("public").enqueue(object : Callback<List<ResponseAllEvents>> {
            override fun onResponse(call: Call<List<ResponseAllEvents>>, response: Response<List<ResponseAllEvents>>) {
                d("Allevents_response-->", "${response.body()}")
                if (response.code()==200) {
                    d("in Response-->", "${response.body()}")
                    val apiData = response.body()
                    if (apiData != null) {
                        rvEventsList.layoutManager = LinearLayoutManager(activity)
                        rvEventsList.adapter = EventAdapter(this@AllEventsFragment, apiData as MutableList<ResponseAllEvents>)

                    }
                }
            }

            override fun onFailure(call: Call<List<ResponseAllEvents>>, t: Throwable) {

                t.message?.let { Log.d("Login_failure-->", it) }

            }
        })
    }

}
