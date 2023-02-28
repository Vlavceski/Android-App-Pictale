package pictale.mk.adapters

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import pictale.mk.DetailsActivity
import pictale.mk.ImageActivity
import pictale.mk.R


class ImageAdapter(val context: Context,
                   private val items: List<Uri>,
                   val eventId: String?,
                   val name: String?,
                   val location: String?) :
    RecyclerView.Adapter<ImageAdapter.ViewHolder>() {
    val data=items
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.image_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        Glide.with(context)
            .load(item)
            .centerCrop()
            .into(holder.picture)

        val id = items[position]
        holder.picture.setOnClickListener {
            d("click-pic","$id")
            val intent = Intent(context, ImageActivity::class.java)
            val uri: Uri = id
            intent.putExtra("imageUri", uri.toString())
            intent.putExtra("name", name)
            intent.putExtra("eventId", eventId)
            intent.putExtra("imageUrisString", data as java.io.Serializable)
            intent.putExtra("location", location)

            context.startActivity(intent)


        }
    }



    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val picture: ImageView = itemView.findViewById(R.id.image_card)

    }

}




