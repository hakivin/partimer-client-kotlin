package com.szechuanstudio.partimer.ui.main.ui.profile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.szechuanstudio.partimer.R
import com.szechuanstudio.partimer.data.model.Model
import kotlinx.android.synthetic.main.position_item.view.*

class PositionAdapter(private val items: List<Model.Position>) :
    RecyclerView.Adapter<PositionAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(position: Model.Position?){
            itemView.tv_tag_position.text = position?.nama_posisi
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.position_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

}