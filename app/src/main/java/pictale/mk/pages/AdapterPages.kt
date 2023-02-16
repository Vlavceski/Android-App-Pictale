package pictale.mk.pages

import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import kotlinx.android.synthetic.main.activity_splash_screen.view.*
import kotlinx.android.synthetic.main.item_event_layout.view.*
import kotlinx.android.synthetic.main.page_item.view.*
import pictale.mk.R

class AdapterPages:PagingDataAdapter<ResponsePages, AdapterPages.ViewHolder>(diffCallback) {


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val card = itemView.findViewById<CardView>(R.id.img_event)
    }

    companion object{
        val diffCallback=object  :DiffUtil.ItemCallback<ResponsePages>(){
            override fun areItemsTheSame(oldItem: ResponsePages, newItem: ResponsePages): Boolean {
                return oldItem.id==newItem.id
            }

            override fun areContentsTheSame(
                oldItem: ResponsePages,
                newItem: ResponsePages
            ): Boolean {
                return oldItem==newItem
            }

        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var currentItem=getItem(position)

        holder.card.apply {
            val imgLink=currentItem?.image
            d("---","$imgLink")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.page_item, parent, false)
        return ViewHolder(view)
    }
}