package pictale.mk.adapters

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import kotlinx.android.synthetic.main.activity_setting.*
import kotlinx.android.synthetic.main.item_event_layout.view.*
import pictale.mk.DetailsActivity
import pictale.mk.R
import pictale.mk.access.APIv3
import pictale.mk.access.ResponseAccessInEvent
import pictale.mk.access.RetrofitInstanceV3
import pictale.mk.auth.API
import pictale.mk.auth.AuthToken
import pictale.mk.auth.RetrofitInstance
import pictale.mk.auth.responses.LoggedResponse
import pictale.mk.events.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create

class PageableEventAdapter(private val context: Context, var data: MutableList<Eventt>)
    : RecyclerView.Adapter<PageableEventAdapter.ViewHolder>() {

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
            openDetails(id)

        }
    }


    fun addAll(events: List<Eventt>) {
        val start = data.size
        data.addAll(events)
        notifyItemRangeInserted(start, events.size)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val card = itemView.findViewById<CardView>(R.id.open_event)
        fun bindData(data: Eventt) {
            itemView.location_event.text = data.location
            itemView.tittle_event.text = data.name

            Glide.with(context)
                .load(data.thumbnailUrl)
                .transform(CircleCrop())
                .into(itemView.img_event)

        }
    }

    private fun openDetails(id: String) {
        val api = RetrofitInstanceV2.getRetrofitInstance().create(APIv2::class.java)
        api.getDetails(id).enqueue(object : Callback<ResponseDetails> {
            override fun onResponse(
                call: Call<ResponseDetails>,
                response: Response<ResponseDetails>
            ) {
                Log.d("Response-Det", "${response.body()}")
                val name=response.body()?.name.toString()
                val location=response.body()?.location.toString()
                val eventId=response.body()?.eventId.toString()
//                val eventFilesList:List<EventFile> = response.body()?.eventFilesList!!


                val imageUrisString = response.body()?.eventFilesList?.mapNotNull { it.urlLink }
                if (imageUrisString != null) {
                    var imageUris: ArrayList<Uri>

                    imageUris = imageUrisString.map { Uri.parse(it) } as ArrayList<Uri>
                    val intent = Intent(context, DetailsActivity::class.java)
                    intent.putExtra("name", name)
                    intent.putExtra("eventId", eventId)
                    intent.putExtra("imageUrisString", imageUris as java.io.Serializable)
                    intent.putExtra("location", location)
                    context.startActivity(intent)
                }


            }

            override fun onFailure(call: Call<ResponseDetails>, t: Throwable) {
                Log.d("Response-Det", "${t.message}")
            }
        })
    }

}








