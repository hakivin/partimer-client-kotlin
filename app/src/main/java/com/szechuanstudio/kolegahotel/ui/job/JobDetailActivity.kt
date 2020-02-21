package com.szechuanstudio.kolegahotel.ui.job

import android.net.ParseException
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
import java.text.SimpleDateFormat
import java.util.*


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
            apply_button.text = getString(R.string.cancel_job)
        }

        apply_button.setOnClickListener {
            presenter.applyJob(job?.id)
            it.isEnabled = false
        }

        job_position.text = job?.posisi?.nama_posisi
        job_count.text = Utils.getQuotaRemaining(job?.kuota, job?.dikerjakan_count, this)
        job_area.text = job?.area
        job_date.text = Utils.convertDate(job?.tanggal_mulai)
        job_time.text = setTime(job?.tanggal_mulai, job?.waktu_mulai, job?.waktu_selesai)
        job_wage.text = job?.bayaran.toString()
        job_height.text = setHeight(job?.tinggi_minimal, job?.tinggi_maksimal)
        job_weight.text = setWeight(job?.berat_minimal, job?.berat_maksimal)
        job_description.text = job?.deskripsi

        job_address.text = job?.hotel?.profile?.alamat
        job_email.text = job?.hotel?.profile?.email
        job_phone.text = job?.hotel?.profile?.nomor_telepon
        job_social_media.text = job?.hotel?.profile?.social_media
        job_website.text = job?.hotel?.profile?.website
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
