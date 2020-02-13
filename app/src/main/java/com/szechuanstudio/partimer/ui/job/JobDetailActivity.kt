package com.szechuanstudio.partimer.ui.job

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.szechuanstudio.partimer.BuildConfig
import com.szechuanstudio.partimer.R
import com.szechuanstudio.partimer.data.model.Model
import com.szechuanstudio.partimer.utils.Constant
import kotlinx.android.synthetic.main.activity_job_detail.*

class JobDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_job_detail)
        val job = intent.getParcelableExtra<Model.Job?>(Constant.KEY_IMAGE_JOB)
        val img = BuildConfig.BASE_URL+job?.hotel?.profile?.foto
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
    }
}
