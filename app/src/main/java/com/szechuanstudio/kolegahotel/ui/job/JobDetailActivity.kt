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
import kotlinx.android.synthetic.main.activity_job_detail.*
import org.jetbrains.anko.toast

class JobDetailActivity : AppCompatActivity(), JobDetailView {

    private lateinit var presenter: JobDetailPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_job_detail)
        val job = intent.getParcelableExtra<Model.Job?>(
            Constant.KEY_IMAGE_JOB)
        val img = BuildConfig.BASE_URL+ '/' + job?.hotel?.profile?.foto
        toolbar.title = job?.hotel?.profile?.nama
        setSupportActionBar(toolbar)
        supportPostponeEnterTransition()

        Picasso.with(this)
            .load(img)
            .into(img_backdrop_detail, object : Callback{
                override fun onSuccess() {
                    supportStartPostponedEnterTransition()
                }
                override fun onError() {
                    supportStartPostponedEnterTransition()
                }
            })

        presenter = JobDetailPresenter(this, RetrofitClient.getInstance(), applicationContext)
    }

    override fun reject(message: String?) {
        toast("Error with message : $message")
    }

    override fun success() {
        toast("Job Successfully Applied")
        onBackPressed()
    }
}