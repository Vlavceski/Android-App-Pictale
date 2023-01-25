package pictale.mk.fragments

import android.os.Bundle
import android.system.Os.open
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager

import kotlinx.android.synthetic.main.fragment_all_events.*
import org.json.JSONException
import org.json.JSONObject
import pictale.mk.Adapter.EventAdapter
import pictale.mk.Data.EventModelClass
import pictale.mk.R
import java.io.IOException
import java.nio.charset.Charset


class AllEventsFragment : Fragment() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val eventsList: ArrayList<EventModelClass> = ArrayList()

        try {
            val obj = JSONObject(getJSONFromAssets()!!)
            val eventsArray = obj.getJSONArray("events")

            for (i in 0 until eventsArray.length()) {
                val event = eventsArray.getJSONObject(i)

                val id = event.getInt("id")
                val name = event.getString("name")
                val location = event.getString("location")
                val firstName = event.getString("firstName")


                val eventDetails = EventModelClass(id, name, location, firstName)

                eventsList.add(eventDetails)
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        // Set the LayoutManager that this RecyclerView will use.
        rvEventsList.layoutManager = LinearLayoutManager(this)
        // Adapter class is initialized and list is passed in the param.
        val itemAdapter = EventAdapter(this, eventsList)
        // adapter instance is set to the recyclerview to inflate the items.
        rvEventsList.adapter = itemAdapter

    }



    private fun getJSONFromAssets(): String? {

        var json: String? = null
        val charset: Charset = Charsets.UTF_8
        try {
            val myEventsJSONFile = assets.open("events.json")
            val size = myEventsJSONFile.available()
            val buffer = ByteArray(size)
            myEventsJSONFile.read(buffer)
            myEventsJSONFile.close()
            json = String(buffer, charset)
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }
        return json
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_events, container, false)
    }


}