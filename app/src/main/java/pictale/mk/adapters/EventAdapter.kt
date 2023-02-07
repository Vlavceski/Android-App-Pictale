package pictale.mk.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_event_layout.view.*
import pictale.mk.R
import pictale.mk.auth.responses.ResponseAllEvents
import pictale.mk.fragments.AllEventsFragment

class EventAdapter(val context: AllEventsFragment, var data: List<ResponseAllEvents?>):
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
        data[position]?.let { holder.bindData(it) }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindData(data: ResponseAllEvents) {
            itemView.location_event.text = data.location
            itemView.tittle_event.text = data.name
        }
    }


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

}