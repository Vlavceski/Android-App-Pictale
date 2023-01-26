package pictale.mk.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_event_layout.view.*
import pictale.mk.Data.EventModelClass
import pictale.mk.R

class EventAdapter (val context: Context, val items: ArrayList<EventModelClass>) :
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

         holder.tvTittle.text = item.tittle
        holder.tvLocation.text = item.location
     }


    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Holds the TextView that will add each item to
        val tvTittle = view.tittle_event
        val tvLocation = view.location_event
       }
}