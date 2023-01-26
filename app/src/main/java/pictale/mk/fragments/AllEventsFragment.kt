package pictale.mk.fragments

import android.os.Bundle
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_all_events.*
import kotlinx.android.synthetic.main.item_event_layout.*
import org.json.JSONException
import pictale.mk.R
import pictale.mk.adapters.EventAdapter
import pictale.mk.data.EventList
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
        try {
            val jsonString = loadJSONFromAsset()
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
    private fun loadJSONFromAsset(): String? {
        var json: String? = null
        try {
            val myUsersJSONFile : InputStream = requireActivity().assets.open("events.json")
            val size = myUsersJSONFile.available()
            val buffer = ByteArray(size)
            myUsersJSONFile.read(buffer)
            myUsersJSONFile.close()
            json = String(buffer, charset("UTF-8"))
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }
        return json
    }
}
