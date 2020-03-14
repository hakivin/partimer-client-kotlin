package com.szechuanstudio.kolegahotel.ui.dashboard.history

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.szechuanstudio.kolegahotel.R
import com.szechuanstudio.kolegahotel.data.model.Model
import com.szechuanstudio.kolegahotel.data.retrofit.RetrofitClient
import kotlinx.android.synthetic.main.activity_history.*
import org.jetbrains.anko.longToast

class HistoryActivity : AppCompatActivity(), HistoryView {

    private lateinit var presenter: HistoryPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        presenter = HistoryPresenter(this, RetrofitClient.getInstance(), applicationContext)
        initToolbar()
        loadContent()
    }

    private fun loadContent(){
        presenter.getJobHistory()
        loading_history.visibility = View.VISIBLE
    }

    private fun initToolbar(){
        supportActionBar?.title = "History"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    override fun reject(message: String) {
        loading_history.visibility = View.GONE
        longToast(message)
    }

    override fun showJobHistory(jobs: List<Model.JobHistory>?) {
        rv_history.layoutManager = LinearLayoutManager(this)
        rv_history.adapter = jobs?.let { HistoryAdapter(it) }
        loading_history.visibility = View.GONE
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}
