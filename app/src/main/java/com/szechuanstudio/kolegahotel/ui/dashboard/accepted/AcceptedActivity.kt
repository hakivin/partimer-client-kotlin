package com.szechuanstudio.kolegahotel.ui.dashboard.accepted

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.szechuanstudio.kolegahotel.R
import com.szechuanstudio.kolegahotel.data.model.Model
import com.szechuanstudio.kolegahotel.data.retrofit.RetrofitClient
import com.szechuanstudio.kolegahotel.ui.main.MainActivity
import kotlinx.android.synthetic.main.activity_accepted.*
import kotlinx.android.synthetic.main.empty_state.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.singleTop
import org.jetbrains.anko.toast

class AcceptedActivity : AppCompatActivity(), AcceptedView {

    private lateinit var presenter: AcceptedPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accepted)
        presenter = AcceptedPresenter(this, RetrofitClient.getInstance(), applicationContext)
        loadContent()
        initToolbar()
        supportFragmentManager
    }

    private fun initToolbar(){
        supportActionBar?.title = "Accepted Jobs"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    private fun loadContent(){
        loading_accepted.visibility = View.VISIBLE
        presenter.getAcceptedJobs()
    }

    override fun reject(message: String?) {
        message?.let { toast(it) }
        loading_accepted.visibility = View.GONE
    }

    override fun showAcceptedJobs(jobs: List<Model.JobAccepted>?) {
        if (jobs != null) {
            if (!jobs.isNullOrEmpty()) {
                rv_accepted.layoutManager = LinearLayoutManager(this)
                rv_accepted.adapter = AcceptedAdapter(jobs, supportFragmentManager)
            } else
                setEmptyState()
        }
        loading_accepted.visibility = View.GONE
    }

    private fun setEmptyState(){
        setContentView(R.layout.empty_state)
        find_job.setOnClickListener {
            navigateUpTo(intentFor<MainActivity>().singleTop())
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}
