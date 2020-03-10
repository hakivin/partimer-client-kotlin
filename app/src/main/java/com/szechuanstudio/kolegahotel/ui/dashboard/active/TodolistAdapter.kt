package com.szechuanstudio.kolegahotel.ui.dashboard.active

import android.graphics.drawable.Animatable2
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.szechuanstudio.kolegahotel.R
import com.szechuanstudio.kolegahotel.data.model.Model
import com.szechuanstudio.kolegahotel.data.retrofit.RetrofitClient
import com.szechuanstudio.kolegahotel.utils.PreferenceUtils
import kotlinx.android.synthetic.main.todolist_item.view.*
import okhttp3.ResponseBody
import org.jetbrains.anko.image
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class TodolistAdapter(private val todolistData: List<Model.ToDoList>, private val checkedData : List<Model.ToDoList>) : RecyclerView.Adapter<TodolistAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var state: Boolean = false

        val crossAnimatedDrawable = itemView.resources.getDrawable(
            R.drawable.animated_clear,
            null
        ) as AnimatedVectorDrawable

        val checkAnimatedDrawable = itemView.resources.getDrawable(
            R.drawable.animated_check,
            null
        ) as AnimatedVectorDrawable

        fun bind(toDoList: Model.ToDoList?){

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                crossAnimatedDrawable.registerAnimationCallback(object :
                    Animatable2.AnimationCallback() {
                    override fun onAnimationEnd(drawable: Drawable) {
                        itemView.fab_todolist_check.setImageDrawable(checkAnimatedDrawable)
                        (itemView.fab_todolist_check.drawable as AnimatedVectorDrawable).start()
                    }
                })
            }

            itemView.tv_todolist_index.text = (adapterPosition + 1).toString()
            itemView.tv_todolist_name.text = toDoList?.nama_pekerjaan
            itemView.fab_todolist_check.setOnClickListener {
                itemView.loading_check.visibility = View.VISIBLE
                RetrofitClient.getInstance().checkTodolist(toDoList?.id, PreferenceUtils.getToken(itemView.context))
                    .enqueue(object : Callback<ResponseBody>{
                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

                        }

                        override fun onResponse(
                            call: Call<ResponseBody>,
                            response: Response<ResponseBody>
                        ) {
                            if (response.isSuccessful){
                                if (state) {
//                                    itemView.fab_todolist_check.image =
//                                        itemView.resources.getDrawable(R.drawable.ic_circle, null)
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        crossAnimatedDrawable.reset()
                                        checkAnimatedDrawable.reset()
                                    }
                                    itemView.fab_todolist_check.setImageDrawable(crossAnimatedDrawable)
                                    state = false
                                }
                                else {
                                    if (itemView.fab_todolist_check.drawable is AnimatedVectorDrawable)
                                        (itemView.fab_todolist_check.drawable as AnimatedVectorDrawable).start()
                                    state = true
                                }
                            } else
                                itemView.context.toast("Something went wrong")
                            itemView.loading_check.visibility = View.GONE
                        }

                    })
            }
        }
        fun check(toDoList: Model.ToDoList?, checkedList: List<Model.ToDoList>?){
            itemView.fab_todolist_check.setImageDrawable(checkAnimatedDrawable)
            if (checkedList != null && toDoList != null) {
                var contain = false
                for (list in checkedList){
                    if (toDoList.id == list.id) {
                        contain = true
                        itemView.fab_todolist_check.setImageDrawable(checkAnimatedDrawable)
                        state = true
                    }
                }
                if (!contain){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        crossAnimatedDrawable.reset()
                        checkAnimatedDrawable.reset()
                    }
                    itemView.fab_todolist_check.setImageDrawable(crossAnimatedDrawable)
                }
            }
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
        holder.check(todolistData[position], checkedData)
    }

}