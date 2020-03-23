package com.szechuanstudio.kolegahotel.ui.dashboard.accepted

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
import com.szechuanstudio.kolegahotel.utils.BaseViewHolder
import com.szechuanstudio.kolegahotel.utils.Constant
import com.szechuanstudio.kolegahotel.utils.Utils
import kotlinx.android.synthetic.main.job_item.view.*

class AcceptedAdapter(private val jobs : ArrayList<Model.JobAccepted>, private val fm : FragmentManager) : RecyclerView.Adapter<BaseViewHolder>() {

    private var isLoaderVisible = false

    class ViewHolder(itemView: View) : BaseViewHolder(itemView) {
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

        override fun clear() {

        }
    }

    class ProgressHolder(itemView: View) : BaseViewHolder(itemView) {
        override fun clear() {
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return when (viewType) {
            VIEW_TYPE_NORMAL -> ViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.job_item, parent, false)
            )
            VIEW_TYPE_LOADING -> ProgressHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.loading_item, parent, false)
            )
            else -> ProgressHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.loading_item, parent, false)
            )
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (isLoaderVisible) {
            if (position == jobs.size - 1) VIEW_TYPE_LOADING else VIEW_TYPE_NORMAL
        } else {
            VIEW_TYPE_NORMAL
        }
    }

    override fun getItemCount(): Int {
        return jobs.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        if (holder is ViewHolder)
            holder.bind(jobs[position], fm)
    }

    fun addItems(postItems: Collection<Model.JobAccepted>) {
        jobs.addAll(postItems)
        notifyDataSetChanged()
    }

    fun addLoading() {
        isLoaderVisible = true
        jobs.add(Model.JobAccepted(null, null, null, null, null,
            null, null, null,null, null,
            null, null, null, null, null, null,
            null, null, null, null, null, null, null, null))
        notifyItemInserted(jobs.lastIndex)
    }

    fun removeLoading() {
        isLoaderVisible = false
        val position: Int = jobs.lastIndex
        val item: Model.JobAccepted? = getItem(position)
        if (item != null) {
            jobs.removeAt(jobs.lastIndex)
            notifyItemRemoved(position)
        }
    }

    private fun getItem(position: Int): Model.JobAccepted? {
        return jobs[position]
    }

    companion object {
        private const val VIEW_TYPE_LOADING = 0
        private const val VIEW_TYPE_NORMAL = 1
    }
}