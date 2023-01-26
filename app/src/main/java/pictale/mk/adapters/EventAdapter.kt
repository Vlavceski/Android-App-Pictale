package pictale.mk.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pictale.mk.R
import pictale.mk.data.EventList

class EventAdapter (val context: Context, var items: MutableList<EventList>):
    RecyclerView.Adapter<EventAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val eventName: TextView
        val eventLocation: TextView
        val eventCreator: TextView

        init {
            //povrzuvanje so xml
            eventName=view.findViewById(R.id.tittle_event)
            eventLocation=view.findViewById(R.id.location_event)
            eventCreator=view.findViewById(R.id.creator_event)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view=LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_event_layout,parent,false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentEvent = items[position]

        holder.eventName.text = currentEvent.name.toString()
        holder.eventLocation.text = currentEvent.location.toString()
        holder.eventCreator.text = currentEvent.createdBy.firstName.toString()

    }

    override fun getItemCount(): Int {
       return items.size
    }

    fun updateData(data: MutableList<EventList>){
        this.items=data
        this.notifyDataSetChanged()

    }

}