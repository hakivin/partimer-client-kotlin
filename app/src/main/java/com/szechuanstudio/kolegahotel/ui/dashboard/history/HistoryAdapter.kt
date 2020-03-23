package com.szechuanstudio.kolegahotel.ui.dashboard.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.szechuanstudio.kolegahotel.BuildConfig
import com.szechuanstudio.kolegahotel.R
import com.szechuanstudio.kolegahotel.data.model.Model
import com.szechuanstudio.kolegahotel.ui.dashboard.history.state.HistoryStateActivity
import com.szechuanstudio.kolegahotel.utils.BaseViewHolder
import com.szechuanstudio.kolegahotel.utils.Constant
import com.szechuanstudio.kolegahotel.utils.Utils
import kotlinx.android.synthetic.main.job_item.view.*
import org.jetbrains.anko.startActivity

class HistoryAdapter(private val jobs: ArrayList<Model.JobHistory>) : RecyclerView.Adapter<BaseViewHolder>() {

    private var isLoaderVisible = false

    class ViewHolder(itemView: View) : BaseViewHolder(itemView) {
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
                it.context.startActivity<HistoryStateActivity>(Constant.KEY_IMAGE_JOB to job)
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
            holder.bind(jobs[position])
    }

    fun addItems(postItems: Collection<Model.JobHistory>) {
        jobs.addAll(postItems)
        notifyDataSetChanged()
    }

    fun addLoading() {
        isLoaderVisible = true
        jobs.add(Model.JobHistory(null, null, null, null, null,
            null, null, null,null, null,
            null, null, null, null, null, null,
            null, null, null, null, null, null,
            null, null, null))
        notifyItemInserted(jobs.lastIndex)
    }

    fun removeLoading() {
        isLoaderVisible = false
        val position: Int = jobs.lastIndex
        val item: Model.JobHistory? = getItem(position)
        if (item != null) {
            jobs.removeAt(jobs.lastIndex)
            notifyItemRemoved(position)
        }
    }

    private fun getItem(position: Int): Model.JobHistory? {
        return jobs[position]
    }

    companion object {
        private const val VIEW_TYPE_LOADING = 0
        private const val VIEW_TYPE_NORMAL = 1
    }
}