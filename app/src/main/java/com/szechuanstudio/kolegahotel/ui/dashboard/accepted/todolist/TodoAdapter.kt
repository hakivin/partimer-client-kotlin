package com.szechuanstudio.kolegahotel.ui.dashboard.accepted.todolist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.szechuanstudio.kolegahotel.R
import com.szechuanstudio.kolegahotel.data.model.Model
import kotlinx.android.synthetic.main.todolist_item.view.*

class TodoAdapter(private val todolistData: List<Model.ToDoList>) : RecyclerView.Adapter<TodoAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(toDoList: Model.ToDoList){
            itemView.fab_container.visibility = View.GONE
            itemView.tv_todolist_index.text = (adapterPosition + 1).toString()
            itemView.tv_todolist_name.text = toDoList.nama_pekerjaan
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.todolist_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return todolistData.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(todolistData[position])
    }
}