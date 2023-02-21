package pictale.mk.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_approve_users.view.*
import pictale.mk.R
import pictale.mk.access.*

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
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindData(data: ResponseUsersWithPermissions) {
            itemView.tv_name.text = data.firstName
            itemView.tv_colab.text=data.collaboration
        }
    }

}