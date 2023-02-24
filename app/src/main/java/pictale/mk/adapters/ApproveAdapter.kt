package pictale.mk.adapters

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_approve_users.view.*
import pictale.mk.DetailsActivity
import pictale.mk.R
import pictale.mk.access.*
import pictale.mk.auth.AuthToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApproveAdapter(
    val context: Context,
    var data: MutableList<ResponseListAll>,
    val eventId: String,
    val name: String?,
    val location: String?,
    val images: List<Uri>,

    ):
    RecyclerView.Adapter<ApproveAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_approve_users, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        data[position].let { holder.bindData(it) }
        val userId = data[position].id

        holder.accept.setOnClickListener {
            opendialog(userId)
        }


    }
    fun opendialog(userId: String) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Approve user as COLLABORATOR")

        builder.setPositiveButton("OK") { dialog, which ->
            // do something when OK button is clicked
            d("Success-e ", userId)
            acceptUser(userId)
        }
        builder.setNegativeButton("Delete") { dialog, which ->
            // do something when OK button is clicked
            rejectUser(userId)
        }
        builder.setNeutralButton("Cancel") { dialog, which ->

        }
        val dialog = builder.create()
        dialog.show()

    }
    private fun rejectUser(userId: String) {
        val token= AuthToken.get(context)
        val api=RetrofitInstanceV3.getRetrofitInstance().create(APIv3::class.java)
        api.rejectAccessInEvent("Bearer $token",eventId,userId)
            .enqueue(object :Callback<ResponseReject>{
                override fun onResponse(
                    call: Call<ResponseReject>,
                    response: Response<ResponseReject>
                ) {
                    if (response.code()==200){
                        d("Success","Deleted!!")

                        val intent = Intent(context, DetailsActivity::class.java)
                        intent.putExtra("name", name)
                        intent.putExtra("eventId", eventId)
                        intent.putExtra("imageUrisString", images as java.io.Serializable)
                        intent.putExtra("location", location)
                        context.startActivity(intent)
                    }
                    else{
                        Toast.makeText(context, response.errorBody().toString(), Toast.LENGTH_LONG).show()

                    }
                }

                override fun onFailure(call: Call<ResponseReject>, t: Throwable) {
                    d("Failure","${t.message}")
                }


            })
    }

    private fun acceptUser(userId: String) {
        var token= AuthToken.get(context)
        val api=RetrofitInstanceV3.getRetrofitInstance().create(APIv3::class.java)
        api.approveAccessInEvent("Bearer $token",eventId,"COLLABORATOR",userId)
            .enqueue(object :Callback<ResponseApproveAccessInEvent>{
            override fun onResponse(
                call: Call<ResponseApproveAccessInEvent>,
                response: Response<ResponseApproveAccessInEvent>
            ) {
                if (response.code()==200){
                    d("Success","Approved!!")
                    //treba da se povika povtorno za sliki
                    d("images","$images")
                    val intent = Intent(context, DetailsActivity::class.java)
                    intent.putExtra("name", name)
                    intent.putExtra("eventId", eventId)
                    intent.putExtra("imageUrisString", images as java.io.Serializable)
                    intent.putExtra("location", location)
                    context.startActivity(intent)
                }
                else{
                    Toast.makeText(context, response.errorBody().toString(), Toast.LENGTH_LONG).show()

                }
            }

            override fun onFailure(call: Call<ResponseApproveAccessInEvent>, t: Throwable) {
                d("Failure","${t.message}")
            }

        })
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val accept = itemView.findViewById<CardView>(R.id.card)
        fun bindData(data: ResponseListAll) {
            itemView.tv_name.text = data.firstName
//            Glide.with(context)
//                .load(data.thumbnailUrl)
//                .transform(CircleCrop())
//                .into(itemView.img_event)

        }
    }



}