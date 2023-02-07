package pictale.mk.fragments

import android.os.Bundle
import android.util.Log
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_all_events.*
import org.json.JSONException
import pictale.mk.R
import pictale.mk.adapters.EventAdapter
import pictale.mk.auth.responses.ResponseAllEvents
import pictale.mk.events.APIv2
import pictale.mk.events.RetrofitInstanceV2
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.io.InputStream


class AllEventsFragment : Fragment() {

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

    private fun fetchData(){
            val api = RetrofitInstanceV2.getRetrofitInstance().create(APIv2::class.java)
            api.getPublicEvents("public").enqueue(object :Callback<List<ResponseAllEvents>>{
                override fun onResponse(call: Call<List<ResponseAllEvents>>, response: Response<List<ResponseAllEvents>>) {
                    d("Allevents_response-->", "${response.body()}")
                    if (response.code()==200) {
                        d("in Response-->", "${response.body()}")
                        val json = response.body()?.toString()

                        d("json-->", "${json}")
                        val list: List<ResponseAllEvents> =
                            Gson().fromJson(json.toString(), object : TypeToken<List<ResponseAllEvents?>?>() {}.type)
                        d("json-->","$list")

                        rvEventsList.layoutManager = LinearLayoutManager(activity)
                        val itemAdapter = EventAdapter(requireActivity().applicationContext,
                            list as MutableList<ResponseAllEvents>
                        )
                        rvEventsList.adapter = itemAdapter

                    }
                }
                override fun onFailure(call: Call<List<ResponseAllEvents>>, t: Throwable) {
                    t.message?.let { Log.d("Login_failure-->", it) }
                }
            })

    }

//
//    private fun fetchData(): String {
//
//        val api = RetrofitInstanceV2.getRetrofitInstance().create(APIv2::class.java)
//        api.getPublicEvents("public").enqueue(object :Callback<List<ResponseAllEvents>>{
//            override fun onResponse(call: Call<List<ResponseAllEvents>>, response: Response<List<ResponseAllEvents>>) {
//                d("Allevents_response-->", "${response.body()}")
//                if (response.code()==200) {
//                    d("in Response-->", "${response.body()}")
//                    apiData = response.body().toString()
////                    if (apiData != null) {
////                        EventAdapter(this@AllEventsFragment,apiData)
////                    }
////                    return apiData
//                }
//            }
//
//            override fun onFailure(call: Call<List<ResponseAllEvents>>, t: Throwable) {
//
//                t.message?.let { Log.d("Login_failure-->", it) }
//
//            }
//        })
//        return apiData.toString()
//    }

}
