package pictale.mk.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log.d
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_fav_events.*
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
        val sharedPreferences = requireContext().getSharedPreferences("preferences", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("token", "")
        d("token-fav","$token")
        if (token != null) {
            fetchData(token)
        }
    }

    private fun fetchData(token:String) {

        val api = RetrofitInstance.getRetrofitInstance().create(API::class.java)
        d("Token_fav--> ","$token")
        api.getFavList("Bearer $token").enqueue(object : Callback<List<ResponseFav>> {
            override fun onResponse(call: Call<List<ResponseFav>>,
                                    response: Response<List<ResponseFav>>
            ) {
                if (response.code()==200) {
                    d("Respo-Fav", "${response.body()}")
                    val apiData = response.body()
                    if (apiData != null) {
                        rvEventsFavList.layoutManager = LinearLayoutManager(activity)
                        rvEventsFavList.adapter = EventFavAdapter(requireContext(), apiData as MutableList<ResponseFav>)
                    }

                }
            }
            override fun onFailure(call: Call<List<ResponseFav>>, t: Throwable) {

                t.message?.let { d("Login_failure-->", it) }

            }
        })

    }

}