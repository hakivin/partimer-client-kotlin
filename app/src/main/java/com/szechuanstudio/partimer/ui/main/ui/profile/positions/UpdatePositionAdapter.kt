package com.szechuanstudio.partimer.ui.main.ui.profile.positions

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.szechuanstudio.partimer.R
import com.szechuanstudio.partimer.data.model.Model
import com.szechuanstudio.partimer.data.retrofit.RetrofitClient
import com.szechuanstudio.partimer.utils.PreferenceUtils
import kotlinx.android.synthetic.main.update_position_item.view.*
import okhttp3.ResponseBody
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdatePositionAdapter(private val context : Context, private val items: List<Model.Position>, private val userPos: List<Model.Position>) :
    RecyclerView.Adapter<UpdatePositionAdapter.ViewHolder>(){

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(position: Model.Position?, context: Context){
            itemView.tv_position_name.text = position?.nama_posisi
            itemView.switch_position.setOnClickListener {
                RetrofitClient.getInstance().selectPosition(position?.id, PreferenceUtils.getToken(context))
                    .enqueue(object : Callback<ResponseBody>{
                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                            context.toast(t.message.toString())
                        }

                        override fun onResponse(
                            call: Call<ResponseBody>,
                            response: Response<ResponseBody>
                        ) {
                            if (response.isSuccessful)
                                context.toast("Success")
                        }
                    })
            }
        }

        fun check(position: Model.Position?, userPos: List<Model.Position>){
            for (ind in userPos){
                if (position?.id == ind.id)
                    itemView.switch_position.isChecked = true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.update_position_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position], context)
        holder.check(items[position], userPos)
    }

}