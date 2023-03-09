package pictale.mk.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.util.Log.d
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_my_events.*
import pictale.mk.R
import pictale.mk.adapters.EventAdapterMyEvents
import pictale.mk.auth.API
import pictale.mk.auth.RetrofitInstance
import pictale.mk.auth.responses.ResponseMyEvents
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyEventsFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_my_events, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState);
        val sharedPreferences = requireContext().getSharedPreferences("preferences", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("token", "")

        if (token != null) {
            fetchData(token)
        }
    }

    private fun fetchData(token:String) {

        val api = RetrofitInstance.getRetrofitInstance().create(API::class.java)
        api.getEvents("Bearer $token").enqueue(object :Callback<List<ResponseMyEvents>>{
            @SuppressLint("SetTextI18n")
            override fun onResponse(
                call: Call<List<ResponseMyEvents>>,
                response: Response<List<ResponseMyEvents>>
            ) {
                if (response.code()==200) {
                    Log.d("in Response-->", "${response.body()}")
                    val apiData = response.body()
                    if (apiData != null && !apiData.isEmpty()) {
                        rvEventsListMyEvent.layoutManager = LinearLayoutManager(activity)
                        rvEventsListMyEvent.adapter = EventAdapterMyEvents(requireContext(), apiData as MutableList<ResponseMyEvents>)
                    }
                    else{
                        empty_text.text = "Empty"
                    }
                }
            }

            override fun onFailure(call: Call<List<ResponseMyEvents>>, t: Throwable) {
                d("Failure ","${t.message}")
            }


        })



    }

}