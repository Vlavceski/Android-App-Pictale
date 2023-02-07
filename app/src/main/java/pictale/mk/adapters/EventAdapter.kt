package pictale.mk.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pictale.mk.R
import pictale.mk.auth.responses.ResponseAllEvents

class EventAdapter(val context: Context, var data: List<ResponseAllEvents?>):
    RecyclerView.Adapter<EventAdapter.ViewHolder>() {


    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        fun bind(item: ResponseAllEvents) {
            eventName.text = item.name
            eventLocation.text = item.description
//            eventCreator.text = item.location

        }

        val eventName: TextView
        val eventLocation: TextView
//        val eventCreator: TextView

        init {
            eventName=view.findViewById(R.id.tittle_event)
            eventLocation=view.findViewById(R.id.location_event)
//            eventCreator=view.findViewById(R.id.creator_event)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view=LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_event_layout,parent,false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = data[position]
        item?.let { holder.bind(it) }

        holder.eventName.text = item?.name.toString()
        holder.eventLocation.text = item?.location.toString()

    }

    override fun getItemCount(): Int = data.size

}