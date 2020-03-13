package com.szechuanstudio.kolegahotel.ui.dashboard.pending

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.szechuanstudio.kolegahotel.R
import com.szechuanstudio.kolegahotel.data.model.Model
import com.szechuanstudio.kolegahotel.data.retrofit.RetrofitClient
import com.szechuanstudio.kolegahotel.ui.main.ui.home.HomeAdapter
import com.szechuanstudio.kolegahotel.ui.main.ui.home.HomePresenter
import com.szechuanstudio.kolegahotel.ui.main.ui.home.HomeView
import com.szechuanstudio.kolegahotel.utils.Constant
import kotlinx.android.synthetic.main.activity_pending.*
import org.jetbrains.anko.toast

class PendingActivity : AppCompatActivity(), HomeView {

    private lateinit var presenter: HomePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pending)
        presenter = HomePresenter(this, RetrofitClient.getInstance(), applicationContext)
        loadContent()
        initToolbar()
    }

    private fun initToolbar(){
        supportActionBar?.title = "Pending Jobs"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    override fun showAllJobs(jobData: List<Model.JobData>?) {
        val adapter = jobData?.let { HomeAdapter(it, null, this) }
        rv_pending.layoutManager = LinearLayoutManager(this)
        rv_pending.adapter = adapter
        loading_pending.visibility = View.GONE
    }

    override fun reject(message: String?) {
        message?.let { toast(it) }
        loading_pending.visibility = View.GONE
    }

    override fun showPositions(positions: List<Model.Position>?) {
        // nothing to do
    }

    private fun loadContent(){
        loading_pending.visibility = View.VISIBLE
        presenter.getPendingJobs()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Constant.APPLY_REQUEST_CODE){
            loadContent()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}