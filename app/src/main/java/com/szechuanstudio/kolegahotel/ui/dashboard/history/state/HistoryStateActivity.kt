package com.szechuanstudio.kolegahotel.ui.dashboard.history.state

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.szechuanstudio.kolegahotel.R
import com.szechuanstudio.kolegahotel.data.model.Model
import com.szechuanstudio.kolegahotel.utils.Constant

class HistoryStateActivity : AppCompatActivity() {

    private lateinit var job: Model.JobHistory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history_state)
        job = intent.getParcelableExtra(Constant.KEY_IMAGE_JOB)!!
        when {
            job.dikerjakan?.first()?.pivot?.status.equals("2") -> setContentView(R.layout.done_state)
            job.dikerjakan?.first()?.pivot?.status.equals("3") -> setContentView(R.layout.verified_state)
            job.dikerjakan?.first()?.pivot?.status.equals("4") -> setContentView(R.layout.finish_state)
        }
    }
}
