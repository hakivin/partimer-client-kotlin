package com.szechuanstudio.kolegahotel.ui.dashboard.accepted

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
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
        val adapter = jobs?.let { AcceptedAdapter(it, supportFragmentManager) }
        rv_accepted.layoutManager = LinearLayoutManager(this)
        rv_accepted.adapter = adapter
        loading_accepted.visibility = View.GONE
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}
