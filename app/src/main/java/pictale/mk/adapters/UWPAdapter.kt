package pictale.mk.adapters

import android.content.Context
import android.content.Intent
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_approve_users.view.*
import pictale.mk.DetailsFavActivity
import pictale.mk.R
import pictale.mk.access.*
import pictale.mk.auth.AuthToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.ProcessBuilder.Redirect

class UWPAdapter(
    val context: Context,
    var data: MutableList<ResponseUsersWithPermissions>,
    val eventId: String):
    RecyclerView.Adapter<UWPAdapter.ViewHolder>() {

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

        holder.delete.setOnClickListener {
           openDialog(userId)
        }

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val delete = itemView.findViewById<CardView>(R.id.card)
        fun bindData(data: ResponseUsersWithPermissions) {
            itemView.tv_name.text = data.firstName
            itemView.tv_colab.text=data.collaboration
        }
    }

    fun openDialog(userId: String) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Delete user")

        builder.setPositiveButton("Delete") { dialog, which ->
            // do something when OK button is clicked
            deleteUser(eventId,userId)
        }
        builder.setNegativeButton("Cancel") { dialog, which ->

        }
        val dialog = builder.create()
        dialog.show()

    }
    private fun deleteUser(eventId: String, userId: String) {
        val token= AuthToken.get(context)
        val api=RetrofitInstanceV3.getRetrofitInstance().create(APIv3::class.java)
        api.removeUserFromEvent("Bearer $token",eventId,userId).enqueue(object :Callback<ResponseRemoveUser>{
            override fun onResponse(
                call: Call<ResponseRemoveUser>,
                response: Response<ResponseRemoveUser>
            ) {
             d("Response","Success-Deleted")
//                Redirect()
            }
            override fun onFailure(call: Call<ResponseRemoveUser>, t: Throwable) {
                d("Failure","${t.message}")
            }

        })
    }

}