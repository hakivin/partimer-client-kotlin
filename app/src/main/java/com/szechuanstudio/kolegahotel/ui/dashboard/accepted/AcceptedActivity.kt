package com.szechuanstudio.kolegahotel.ui.dashboard.accepted

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.szechuanstudio.kolegahotel.R
import com.szechuanstudio.kolegahotel.data.model.Model
import com.szechuanstudio.kolegahotel.data.retrofit.RetrofitClient
import kotlinx.android.synthetic.main.activity_accepted.*
import org.jetbrains.anko.toast

class AcceptedActivity : AppCompatActivity(), AcceptedView {

    private lateinit var presenter: AcceptedPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accepted)
        presenter = AcceptedPresenter(this, RetrofitClient.getInstance(), applicationContext)
        presenter.getAcceptedJobs()
        initToolbar()
    }

    private fun initToolbar(){
        supportActionBar?.title = "Accepted Jobs"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    override fun reject(message: String?) {
        message?.let { toast(it) }
    }

    override fun showAcceptedJobs(jobs: List<Model.JobAccepted>?) {
        val adapter = jobs?.let { AcceptedAdapter(it, this) }
        rv_accepted.layoutManager = LinearLayoutManager(this)
        rv_accepted.adapter = adapter
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}
