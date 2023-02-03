package pictale.mk.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_setting.*
import kotlinx.android.synthetic.main.fragment_all_events.*
import kotlinx.android.synthetic.main.item_event_layout.*
import org.json.JSONException
import pictale.mk.R
import pictale.mk.adapters.EventAdapter
import pictale.mk.auth.API
import pictale.mk.auth.RetrofitInstance
import pictale.mk.auth.responses.LoggedResponse
import pictale.mk.data.EventList
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
        val token = arguments?.getString("token")

        try {

            val jsonString = getAllEvents(token)
            val list: List<EventList> =
                Gson().fromJson(jsonString.toString(), object : TypeToken<List<EventList?>?>() {}.type)
            d("json-->","$list")

            rvEventsList.layoutManager = LinearLayoutManager(activity)
            val itemAdapter = EventAdapter(requireActivity().applicationContext,
                list as MutableList<EventList>
            )
            rvEventsList.adapter = itemAdapter
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }
    private fun getAllEvents(token: String?): String? {
        var json: String? = null
        val api = RetrofitInstance.getRetrofitInstance().create(API::class.java)

        d("AllEvent_token-->", "$token")

        api.getClient("Bearer $token").enqueue(object : Callback<LoggedResponse> {
            override fun onResponse(call: Call<LoggedResponse>, response: Response<LoggedResponse>) {
                d("Profile_response-->", "${response.body()}")
                if (response.code()==200) {
                    d("in Response-->", response.body()?.email.toString())
                    json= response.body().toString()
                }
            }

            override fun onFailure(call: Call<LoggedResponse>, t: Throwable) {

                t.message?.let { Log.d("Login_failure-->", it) }

            }
        })
        return json
    }

}
