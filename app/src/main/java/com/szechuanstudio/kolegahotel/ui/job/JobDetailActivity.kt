package com.szechuanstudio.kolegahotel.ui.job

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.szechuanstudio.kolegahotel.BuildConfig
import com.szechuanstudio.kolegahotel.R
import com.szechuanstudio.kolegahotel.data.model.Model
import com.szechuanstudio.kolegahotel.data.retrofit.RetrofitClient
import com.szechuanstudio.kolegahotel.utils.Constant
import com.szechuanstudio.kolegahotel.utils.Utils
import kotlinx.android.synthetic.main.activity_job_detail.*
import kotlinx.android.synthetic.main.content_job_detail.*
import org.jetbrains.anko.toast

class JobDetailActivity : AppCompatActivity(), JobDetailView {

    private lateinit var presenter: JobDetailPresenter
    private var job : Model.Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_job_detail)
        initToolbar()
        initContent()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    private fun initContent() {
        presenter = JobDetailPresenter(this, RetrofitClient.getInstance(), applicationContext)

        if (job?.isApplied!!) {
            apply_button.setBackgroundColor(resources.getColor(R.color.red_500))
            apply_button.text = "Cancel Job"
        }

        apply_button.setOnClickListener {
            presenter.applyJob(job?.id)
            it.isEnabled = false
        }

        job_position.text = job?.posisi?.nama_posisi
        job_count.text = "${(job?.dikerjakan_count?.let { job?.kuota?.minus(it) }).toString()} left"
        job_area.text = job?.area
        job_date.text = Utils.convertDate(job?.tanggal_mulai)
        job_time.text = "${job?.waktu_mulai} until ${job?.waktu_selesai}"
        job_wage.text = job?.bayaran.toString()
        job_height.text = "${job?.tinggi_minimal} - ${job?.tinggi_maksimal}"
        job_weight.text = "${job?.berat_minimal} - ${job?.berat_maksimal}"
        job_description.text = job?.deskripsi
    }

    private fun initToolbar() {
        job = intent.getParcelableExtra<Model.Job?>(
            Constant.KEY_IMAGE_JOB
        )
        val img = BuildConfig.BASE_URL + '/' + job?.hotel?.profile?.foto
        toolbar.title = job?.hotel?.profile?.nama
        setSupportActionBar(toolbar)
        supportPostponeEnterTransition()

        Picasso.with(this)
            .load(img)
            .into(img_backdrop_detail, object : Callback {
                override fun onSuccess() {
                    supportStartPostponedEnterTransition()
                }

                override fun onError() {
                    supportStartPostponedEnterTransition()
                }
            })
    }

    override fun error(message: String?) {
        toast("Error with message : $message")
        apply_button.isEnabled = true
    }

    override fun reject() {
        toast("You do not meet the requirement")
        apply_button.isEnabled = true
    }

    override fun success() {
        if (job != null) {
            if (job!!.isApplied!!)
                toast("Job Cancelled")
            else
                toast("Job Successfully Applied")
        }
        onBackPressed()
        apply_button.isEnabled = true
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}
