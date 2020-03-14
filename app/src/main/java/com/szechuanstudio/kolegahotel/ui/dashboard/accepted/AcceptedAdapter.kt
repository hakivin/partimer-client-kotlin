package com.szechuanstudio.kolegahotel.ui.dashboard.accepted

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.szechuanstudio.kolegahotel.BuildConfig
import com.szechuanstudio.kolegahotel.R
import com.szechuanstudio.kolegahotel.data.model.Model
import com.szechuanstudio.kolegahotel.ui.dashboard.accepted.todolist.TodolistFragment
import com.szechuanstudio.kolegahotel.utils.Constant
import com.szechuanstudio.kolegahotel.utils.Utils
import kotlinx.android.synthetic.main.job_item.view.*

class AcceptedAdapter(private val jobs : List<Model.JobAccepted>, private val fm : FragmentManager) : RecyclerView.Adapter<AcceptedAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(job : Model.JobAccepted, fm : FragmentManager){
            if (job.foto.isNullOrBlank())
                Picasso.with(itemView.context).load(BuildConfig.BASE_URL+ '/' + job.hotel?.profile?.foto).into(itemView.img_cover_job)
            else
                Picasso.with(itemView.context).load(BuildConfig.BASE_URL+ '/' + job.foto).into(itemView.img_cover_job)
            itemView.tv_date_job.text = Utils.convertDate(job.tanggal_mulai)
            itemView.tv_hotel_name_job.text = job.hotel?.profile?.nama
            itemView.tv_quota_job.text = Utils.getQuotaRemaining(job.kuota, job.dikerjakan_count, itemView.context)
            itemView.tv_position_job.text = job.posisi?.nama_posisi

            itemView.setOnClickListener {
                val dialog = TodolistFragment()
                val bundle = Bundle()
                bundle.putInt(Constant.KEY_IMAGE_JOB, job.id!!)
                dialog.arguments = bundle
                dialog.show(fm, "todolist_tag")
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
        holder.bind(jobs[position], fm)
    }
}