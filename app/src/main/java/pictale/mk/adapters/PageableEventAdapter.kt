package pictale.mk.adapters

import android.content.Context
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_event_layout.view.*
import pictale.mk.R
import pictale.mk.events.Event

class PageableEventAdapter(private val context: Context, var data: MutableList<Event>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_event_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        data[position].let { holder.bindData(it) }

    }

    override fun getItemCount(): Int {
        d("---data ","${data.size}")
        return data.size
    }

//    fun addAll(events: List<Content>) {
//        eventList.addAll(events)
//    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val card = itemView.findViewById<CardView>(R.id.open_event)
        fun bindData(data: Event) {
            itemView.location_event.text = data.name
//            itemView.tittle_event.text = data.name
//
//            Glide.with(context)
//                .load(data.thumbnailUrl)
//                .transform(CircleCrop())
//                .into(itemView.img_event)

        }
    }
}






