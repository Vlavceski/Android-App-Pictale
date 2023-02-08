package pictale.mk.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_all_events.*
import pictale.mk.R
import pictale.mk.adapters.EventFavAdapter
import pictale.mk.auth.API
import pictale.mk.auth.RetrofitInstance
import pictale.mk.auth.responses.ResponseFav
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FavEventsFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_fav_events, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState);

//        fetchData()

    }
/*
    private fun fetchData() {

        val api = RetrofitInstance.getRetrofitInstance().create(API::class.java)

        api.getFavList().enqueue(object : Callback<List<ResponseFav>> {
            override fun onResponse(call: Call<List<ResponseFav>>,
                                    response: Response<List<ResponseFav>>) {
                if (response.code()==200) {
                    Log.d("in Response-->", "${response.body()}")
                    val apiData = response.body()
                    if (apiData != null) {
                        rvEventsList.layoutManager = LinearLayoutManager(activity)
                        rvEventsList.adapter = EventFavAdapter(this@FavEventsFragment, apiData as MutableList<ResponseFav>)
                    }
                }
            }
            override fun onFailure(call: Call<List<ResponseFav>>, t: Throwable) {

                t.message?.let { Log.d("Login_failure-->", it) }

            }
        })

    }
 */



}