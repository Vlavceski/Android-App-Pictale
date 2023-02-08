package pictale.mk.adapters

import android.content.Intent
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_event_layout.view.*
import pictale.mk.DetailsActivity
import pictale.mk.R
import pictale.mk.auth.responses.ResponseAllEvents
import pictale.mk.events.APIv2
import pictale.mk.events.ResponseDetails
import pictale.mk.events.RetrofitInstanceV2
import pictale.mk.fragments.AllEventsFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EventAdapter(val context: AllEventsFragment, var data: MutableList<ResponseAllEvents>):
    RecyclerView.Adapter<EventAdapter.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_event_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        data[position].let { holder.bindData(it) }
        val id = data[position].eventId
        holder.card.setOnClickListener {
            d("id--->" ,"$id")
            openDetails(id)
        }


    }



    private fun openDetails(id: String) {
        val api = RetrofitInstanceV2.getRetrofitInstance().create(APIv2::class.java)
        api.getDetails(id).enqueue(object :Callback<ResponseDetails>{
            override fun onResponse(
                call: Call<ResponseDetails>,
                response: Response<ResponseDetails>
            ) {
                d("Response-Det","${response.body()}")
                val name=response.body()?.name.toString()
                val location=response.body()?.location.toString()
//                goToDetails(name,location)
            }

            override fun onFailure(call: Call<ResponseDetails>, t: Throwable) {

                d("Response-Det","${t.message}")
            }
        })
    }

    private fun goToDetails(name: String, location: String) {
//        val intent = Intent(context, DetailsActivity::class.java)
//        intent.putExtra("name", name)
//        intent.putExtra("location", location)
//        context.startActivity(intent)
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val card = itemView.findViewById<CardView>(R.id.open_event)
        fun bindData(data: ResponseAllEvents) {
            itemView.location_event.text = data.location
            itemView.tittle_event.text = data.name

        }
    }

/*
//
//    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
//        fun bind(item: ResponseAllEvents) {
//            eventName.text = item.name
//            eventLocation.text = item.location
//
//        }
//
//        val eventName: TextView
//        val eventLocation: TextView
//
//        init {
//            eventName=view.findViewById(R.id.tittle_event)
//            eventLocation=view.findViewById(R.id.location_event)
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val view=LayoutInflater
//            .from(parent.context)
//            .inflate(R.layout.item_event_layout,parent,false)
//
//        return ViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//
//        val item = data[position]
//        item?.let { holder.bind(it) }
//
//        holder.eventName.text = item?.name.toString()
//        holder.eventLocation.text = item?.location.toString()
//
//    }
//
//    override fun getItemCount(): Int = data.size
 */

}