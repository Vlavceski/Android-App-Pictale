package pictale.mk.fragments

import android.os.Bundle
import android.util.Log
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_all_events.*
import kotlinx.android.synthetic.main.item_event_layout.*
import pictale.mk.R
import pictale.mk.adapters.EventAdapter
import pictale.mk.adapters.PageableEventAdapter
import pictale.mk.auth.responses.ResponseAllEvents
import pictale.mk.events.APIv2
import pictale.mk.events.Event
import pictale.mk.events.ResponseAllEventsPages
import pictale.mk.events.RetrofitInstanceV2
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


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

    private fun searchItems(it: String) {
        d("SearchItems-->", "Good!")
        val api = RetrofitInstanceV2.getRetrofitInstance().create(APIv2::class.java)
        api.searchItem(it).enqueue(object : Callback<List<ResponseAllEvents>>{
            override fun onResponse(
                call: Call<List<ResponseAllEvents>>,
                response: Response<List<ResponseAllEvents>>
            ) {
                val apiData = response.body()
                if (apiData != null) {
                    rvEventsList.layoutManager = LinearLayoutManager(activity)
                    rvEventsList.adapter = EventAdapter(requireContext(), apiData as MutableList<ResponseAllEvents>)
                }
            }

            override fun onFailure(call: Call<List<ResponseAllEvents>>, t: Throwable) {
                t.message?.let { Log.d("Login_failure-->", it) }
            }

        })
    }


    override fun  onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState);


        tvSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { searchItems(it) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
//                fetchData()
                return false
            }
        })
        fetchDataWithPages()
    }

    private fun fetchDataWithPages(){

        val api=RetrofitInstanceV2.getRetrofitInstance().create(APIv2::class.java)
        val valuePageable = "public"
        var currentPage = 0
        val sizePageable =3
        var totalNumberOfPages:Int
        api.getPages(eventPublicityType = valuePageable, page = currentPage, size = sizePageable)
            .enqueue(object: Callback<ResponseAllEventsPages>{
                override fun onResponse(call: Call<ResponseAllEventsPages>, response: Response<ResponseAllEventsPages>) {
                    val eventsList = mutableListOf<Event>()
                    if (response.code() == 200) {
                        val pageableEvents = response.body()
                        if (pageableEvents != null) {
                            eventsList.addAll(pageableEvents.content)
                            recyclerView = view!!.findViewById(R.id.rvEventsList)
                            recyclerView.layoutManager = LinearLayoutManager(requireContext())
                            if (currentPage == 0) {
                                recyclerView.adapter = PageableEventAdapter(requireContext(), eventsList)
                                totalNumberOfPages = pageableEvents.totalPages
                            }
//                            else {
////                                (recyclerView.adapter as PageableEventAdapter).addAll(pageableEvents.content)
//                            }
                            if (!pageableEvents.last) {
                                currentPage++
                                eventsList.clear()
//                                listAllEventsPageable()
                            }
                        }
                    } else {
                        val errorMessage = (response.errorBody())?.string()
                        if (errorMessage != null) {
                            Log.d("HomeFragment->ERROR", errorMessage)
                        }
                    }
                    }

                override fun onFailure(call: Call<ResponseAllEventsPages>, t: Throwable) {
                    d("Failure","${t.message}")
                }
            })
    }



    private fun fetchData() {

        val api = RetrofitInstanceV2.getRetrofitInstance().create(APIv2::class.java)
        api.getPublicEvents("public").enqueue(object : Callback<List<ResponseAllEvents>> {
            override fun onResponse(call: Call<List<ResponseAllEvents>>, response: Response<List<ResponseAllEvents>>) {
                if (response.code()==200) {
                    d("in Response-->", "${response.body()}")
                    val apiData = response.body()
                    if (apiData != null) {
                        rvEventsList.layoutManager = LinearLayoutManager(activity)
                        rvEventsList.adapter = EventAdapter(requireContext(), apiData as MutableList<ResponseAllEvents>)
                    }
                }
            }
            override fun onFailure(call: Call<List<ResponseAllEvents>>, t: Throwable) {

                t.message?.let { Log.d("Login_failure-->", it) }

            }
        })
    }

}
