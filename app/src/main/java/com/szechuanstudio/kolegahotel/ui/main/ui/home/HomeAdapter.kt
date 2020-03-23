package com.szechuanstudio.kolegahotel.ui.main.ui.home

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.szechuanstudio.kolegahotel.BuildConfig
import com.szechuanstudio.kolegahotel.R
import com.szechuanstudio.kolegahotel.data.model.Model
import com.szechuanstudio.kolegahotel.ui.job.JobDetailActivity
import com.szechuanstudio.kolegahotel.utils.BaseViewHolder
import com.szechuanstudio.kolegahotel.utils.Constant
import com.szechuanstudio.kolegahotel.utils.Utils
import kotlinx.android.synthetic.main.job_item.view.*

class HomeAdapter(private val jobData : ArrayList<Model.JobData>, private val act: Fragment?, private val act2: Activity?) : RecyclerView.Adapter<BaseViewHolder>() {

    private var isLoaderVisible = false

    class ViewHolder(itemView: View) : BaseViewHolder(itemView) {
        fun bind(jobData : Model.JobData, act: Fragment){
            if (jobData.foto.isNullOrBlank())
                Picasso.with(itemView.context).load(BuildConfig.BASE_URL+ '/' + jobData.hotel?.profile?.foto).into(itemView.img_cover_job)
            else
                Picasso.with(itemView.context).load(BuildConfig.BASE_URL+ '/' + jobData.foto).into(itemView.img_cover_job)
            itemView.tv_date_job.text = Utils.convertDate(jobData.tanggal_mulai)
            itemView.tv_hotel_name_job.text = jobData.hotel?.profile?.nama
            itemView.tv_quota_job.text = Utils.getQuotaRemaining(jobData.kuota, jobData.dikerjakan_count, act.context!!)
            itemView.tv_position_job.text = jobData.posisi?.nama_posisi
            itemView.setOnClickListener {
                val intent =
                    Intent(act.context, JobDetailActivity::class.java)
                intent.putExtra(Constant.KEY_IMAGE_JOB, jobData)
                val options =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        act.requireActivity(),
                        itemView.img_cover_job,
                        ViewCompat.getTransitionName(itemView.img_cover_job)!!
                    )
                act.startActivityForResult(intent, Constant.APPLY_REQUEST_CODE, options.toBundle())
            }
        }

        fun bind(jobData : Model.JobData, act: Activity){
            if (jobData.foto.isNullOrBlank())
                Picasso.with(itemView.context).load(BuildConfig.BASE_URL+ '/' + jobData.hotel?.profile?.foto).into(itemView.img_cover_job)
            else
                Picasso.with(itemView.context).load(BuildConfig.BASE_URL+ '/' + jobData.foto).into(itemView.img_cover_job)
            itemView.tv_date_job.text = Utils.convertDate(jobData.tanggal_mulai)
            itemView.tv_hotel_name_job.text = jobData.hotel?.profile?.nama
            itemView.tv_quota_job.text = Utils.getQuotaRemaining(jobData.kuota, jobData.dikerjakan_count, act.baseContext)
            itemView.tv_position_job.text = jobData.posisi?.nama_posisi
            itemView.setOnClickListener {
                val intent =
                    Intent(act.baseContext, JobDetailActivity::class.java)
                intent.putExtra(Constant.KEY_IMAGE_JOB, jobData)
                val options =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        act,
                        itemView.img_cover_job,
                        ViewCompat.getTransitionName(itemView.img_cover_job)!!
                    )
                act.startActivityForResult(intent, Constant.APPLY_REQUEST_CODE, options.toBundle())
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
            if (position == jobData.size - 1) VIEW_TYPE_LOADING else VIEW_TYPE_NORMAL
        } else {
            VIEW_TYPE_NORMAL
        }

    }
    override fun getItemCount(): Int {
        return jobData.size
    }

    fun addItems(postItems: Collection<Model.JobData>) {
        jobData.addAll(postItems)
        notifyDataSetChanged()
    }

    fun addLoading() {
        isLoaderVisible = true
        jobData.add(Model.JobData())
        notifyItemInserted(jobData.lastIndex)
    }

    fun removeLoading() {
        isLoaderVisible = false
        val position: Int = jobData.lastIndex
        val item: Model.JobData? = getItem(position)
        if (item != null) {
            jobData.removeAt(jobData.lastIndex)
            notifyItemRemoved(position)
        }
    }

    fun clear() {
        jobData.clear()
        notifyDataSetChanged()
    }

    private fun getItem(position: Int): Model.JobData? {
        return jobData[position]
    }

    companion object {
        private const val VIEW_TYPE_LOADING = 0
        private const val VIEW_TYPE_NORMAL = 1
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onBind(position)
        if (act != null) {
            if (holder is ViewHolder)
                holder.bind(jobData[position], act)
        }
        else if (act2 != null){
            if (holder is ViewHolder)
                holder.bind(jobData[position], act2)
        }
    }
}