package com.szechuanstudio.kolegahotel.ui.main.ui.home

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
import com.szechuanstudio.kolegahotel.utils.Constant
import com.szechuanstudio.kolegahotel.utils.Utils
import kotlinx.android.synthetic.main.job_item.view.*

class HomeAdapter(private val jobs : List<Model.Job>, private val act: Fragment) : RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(job : Model.Job, act: Fragment){
            Picasso.with(itemView.context).load(BuildConfig.BASE_URL+ '/' + job.hotel?.profile?.foto).into(itemView.img_cover_job)
            itemView.tv_date_job.text = Utils.convertDate(job.tanggal_mulai)
            itemView.tv_hotel_name_job.text = job.hotel?.profile?.nama
            itemView.tv_quota_job.text = Utils.getQuotaRemaining(job.kuota, job.dikerjakan_count, act.context!!)
            itemView.tv_position_job.text = job.posisi?.nama_posisi
            itemView.setOnClickListener {
                val intent =
                    Intent(act.context, JobDetailActivity::class.java)
                intent.putExtra(Constant.KEY_IMAGE_JOB, job)
                val options =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        act.requireActivity(),
                        itemView.img_cover_job,
                        ViewCompat.getTransitionName(itemView.img_cover_job)!!
                    )
                act.startActivityForResult(intent, Constant.APPLY_REQUEST_CODE, options.toBundle())
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
        holder.bind(jobs[position], act)
    }
}