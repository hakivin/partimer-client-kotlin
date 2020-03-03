package com.szechuanstudio.kolegahotel.ui.job

import android.net.ParseException
import android.os.Build
import android.os.Bundle
import android.text.Html
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
import java.text.SimpleDateFormat
import java.util.*


class JobDetailActivity : AppCompatActivity(), JobDetailView {

    private lateinit var presenter: JobDetailPresenter
    private var jobData : Model.JobData? = null

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

        if (jobData?.isApplied!!) {
            apply_button.setBackgroundColor(resources.getColor(R.color.red_500))
            apply_button.text = getString(R.string.cancel_job)
        }

        apply_button.setOnClickListener {
            presenter.applyJob(jobData?.id)
            it.isEnabled = false
        }

        job_position.text = jobData?.posisi?.nama_posisi
        job_count.text = Utils.getQuotaRemaining(jobData?.kuota, jobData?.dikerjakan_count, this)
        job_area.text = jobData?.area
        job_date.text = Utils.convertDate(jobData?.tanggal_mulai)
        job_time.text = setTime(jobData?.tanggal_mulai, jobData?.waktu_mulai, jobData?.waktu_selesai)
        job_wage.text = jobData?.bayaran.toString()
        job_height.text = setHeight(jobData?.tinggi_minimal, jobData?.tinggi_maksimal)
        job_weight.text = setWeight(jobData?.berat_minimal, jobData?.berat_maksimal)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            job_description.text = Html.fromHtml(jobData?.deskripsi, Html.FROM_HTML_MODE_COMPACT)
        } else {
            job_description.text = Html.fromHtml(jobData?.deskripsi)
        }

        job_address.text = jobData?.hotel?.profile?.alamat
        job_email.text = jobData?.hotel?.profile?.email
        job_phone.text = jobData?.hotel?.profile?.nomor_telepon
        job_social_media.text = jobData?.hotel?.profile?.social_media
        job_website.text = jobData?.hotel?.profile?.website
    }

    private fun setHeight(minHeight : Int?, maxHeight : Int?) : CharSequence?{
        if (minHeight == null && maxHeight == null)
            return "Not specified"

        val min: CharSequence? = if (minHeight != null)
            "$minHeight cm"
        else
            "Not specified"
        val max: CharSequence? = if (maxHeight != null)
            "$maxHeight cm"
        else
            "Not specified"
        return "$min - $max"
    }

    private fun setWeight(minWeight : Int?, maxWeight : Int?) : CharSequence?{
        if (minWeight == null && maxWeight == null)
            return "Not specified"

        val min: CharSequence? = if (minWeight != null)
            "$minWeight cm"
        else
            "Not specified"
        val max: CharSequence? = if (maxWeight != null)
            "$maxWeight cm"
        else
            "Not specified"
        return "$min - $max"
    }

    private fun setTime(workingDate : String?, startTime : String?, endTime : String?): CharSequence? {
        val strtTime = "$workingDate $startTime"
        val nTime = "$workingDate $endTime"
        val st = StringTokenizer(strtTime)
        val en = StringTokenizer(nTime)
        st.nextToken()
        en.nextToken()
        val timeStart = st.nextToken()
        val timeEnd = en.nextToken()

        val sdf = SimpleDateFormat("hh:mm:ss", Locale.getDefault())
        val sdfs = SimpleDateFormat("hh:mm a", Locale.getDefault())
        val dtStart: Date
        val dtEnd: Date
        try {
            dtStart = sdf.parse(timeStart)!!
            dtEnd = sdf.parse(timeEnd)!!

            return "${sdfs.format(dtStart)} until ${sdfs.format(dtEnd)}" // <-- I got result here
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return null
    }

    private fun initToolbar() {
        jobData = intent.getParcelableExtra<Model.JobData?>(
            Constant.KEY_IMAGE_JOB
        )
        val img = BuildConfig.BASE_URL + '/' + jobData?.hotel?.profile?.foto
        toolbar.title = jobData?.hotel?.profile?.nama
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

    override fun reject(message: String?) {
        message?.let { toast(it) }
        apply_button.isEnabled = true
    }

    override fun success() {
        if (jobData != null) {
            if (jobData!!.isApplied!!)
                toast("Job Cancelled")
            else
                toast("Job Successfully Applied")
        }
        setResult(Constant.APPLY_REQUEST_CODE)
        onBackPressed()
        apply_button.isEnabled = true
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}
