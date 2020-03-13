package com.szechuanstudio.kolegahotel.ui.dashboard.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.szechuanstudio.kolegahotel.BuildConfig
import com.szechuanstudio.kolegahotel.R
import com.szechuanstudio.kolegahotel.data.model.Model
import com.szechuanstudio.kolegahotel.utils.Utils
import kotlinx.android.synthetic.main.job_item.view.*

class HistoryAdapter(private val jobs: List<Model.JobHistory>) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(job: Model.JobHistory){
            if (job.foto.isNullOrBlank())
                Picasso.with(itemView.context).load(BuildConfig.BASE_URL+ '/' + job.hotel?.profile?.foto).into(itemView.img_cover_job)
            else
                Picasso.with(itemView.context).load(BuildConfig.BASE_URL+ '/' + job.foto).into(itemView.img_cover_job)
            itemView.tv_date_job.text = Utils.convertDate(job.tanggal_mulai)
            itemView.tv_hotel_name_job.text = job.hotel?.profile?.nama
            itemView.tv_quota_job.text = Utils.getQuotaRemaining(job.kuota, job.dikerjakan_count, itemView.context)
            itemView.tv_position_job.text = job.posisi?.nama_posisi

            itemView.setOnClickListener {

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.job_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return jobs.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(jobs[position])
    }
}