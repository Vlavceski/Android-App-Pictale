package pictale.mk.adapters

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_event_layout.view.*
import pictale.mk.DetailsActivity
import pictale.mk.DetailsFavActivity
import pictale.mk.R
import pictale.mk.auth.responses.ResponseFav
import pictale.mk.events.APIv2
import pictale.mk.events.EventFile
import pictale.mk.events.ResponseDetails
import pictale.mk.events.RetrofitInstanceV2
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EventFavAdapter(val context: Context, var data: MutableList<ResponseFav>):
    RecyclerView.Adapter<EventFavAdapter.ViewHolder>() {


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
                val eventId=response.body()?.eventId.toString()

                val imageUrisString = response.body()?.eventFilesList?.mapNotNull { it.urlLink }
                if (imageUrisString != null) {
                    var imageUris: ArrayList<Uri>
                    imageUris = imageUrisString.map { Uri.parse(it) } as ArrayList<Uri>
                    val intent = Intent(context, DetailsFavActivity::class.java)
                    intent.putExtra("name", name)
                    intent.putExtra("eventId", eventId)
                    intent.putExtra("imageUrisString", imageUris as java.io.Serializable)
                    intent.putExtra("location", location)
                    context.startActivity(intent)
                }
            }

            override fun onFailure(call: Call<ResponseDetails>, t: Throwable) {

                d("Response-Det","${t.message}")
            }
        })
    }



    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val card = itemView.findViewById<CardView>(R.id.open_event)
        fun bindData(data: ResponseFav) {
            itemView.location_event.text = data.location
            itemView.tittle_event.text = data.name

        }
    }

}