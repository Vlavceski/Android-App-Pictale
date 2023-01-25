package pictale.mk.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_event_layout.view.*
import pictale.mk.Data.EventModelClass
import pictale.mk.R

class EventAdapter(val context: Context, private val items: ArrayList<EventModelClass>) :
    RecyclerView.Adapter<EventAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_event_layout,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = items.get(position)

//        holder.tvId.text = item.id.toString()
        holder.tvName.text = item.name
        holder.tvLocation.text=item.location
        holder.tvCreator.text=item.firstName
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
//        val tvId=view.id
        val tvName = view.tittle_event
        val tvLocation = view.location_event
        val tvCreator = view.creator_event

    }
}