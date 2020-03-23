package com.szechuanstudio.kolegahotel.ui.dashboard.history

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.szechuanstudio.kolegahotel.R
import com.szechuanstudio.kolegahotel.data.model.Model
import com.szechuanstudio.kolegahotel.data.retrofit.RetrofitClient
import com.szechuanstudio.kolegahotel.ui.main.MainActivity
import com.szechuanstudio.kolegahotel.utils.PaginationScrollListener
import com.szechuanstudio.kolegahotel.utils.PaginationScrollListener.Companion.PAGE_START
import com.szechuanstudio.kolegahotel.utils.Utils
import kotlinx.android.synthetic.main.activity_history.*
import kotlinx.android.synthetic.main.empty_state.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.singleTop

class HistoryActivity : AppCompatActivity(), HistoryView, SwipeRefreshLayout.OnRefreshListener {

    private lateinit var presenter: HistoryPresenter
    private lateinit var adapter: HistoryAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var historyAttrib: Model.PageAttrib

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        presenter = HistoryPresenter(this, RetrofitClient.getInstance(), applicationContext)
        initToolbar()
        loadContent()
        layoutManager = LinearLayoutManager(this)
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
//        longToast(message)
        Utils.setOfflineState(this, this)
    }

    override fun showJobHistory(jobs: Model.HistoryPaginate?) {
        setContentView(R.layout.activity_history)
        if (jobs != null) {
            if (!jobs.data.isNullOrEmpty()) {
                adapter = HistoryAdapter(jobs.data as ArrayList<Model.JobHistory>)
                if (jobs.current_page!! < jobs.last_page!!)
                    adapter.addLoading()
                rv_history.layoutManager = layoutManager
                rv_history.adapter = adapter
                refresh_history.setOnRefreshListener(this)
                initRvListener()
                historyAttrib =
                    Model.PageAttrib(jobs.current_page, false, jobs.last_page, false)
                loading_history.visibility = View.GONE
                refresh_history.isRefreshing = false
            } else
                setEmptyState()
        }
    }

    override fun addJobHistory(jobs: Model.HistoryPaginate?) {
        historyAttrib.currentPage = jobs?.current_page!!
        if (historyAttrib.currentPage != PAGE_START) adapter.removeLoading()
        jobs.data?.let { adapter.addItems(it) }
        if (historyAttrib.currentPage < historyAttrib.totalPage)
            adapter.addLoading()
        else
            historyAttrib.isLastPage = true
        historyAttrib.isLoading = false
    }

    private fun setEmptyState(){
        setContentView(R.layout.empty_state)
        find_job.setOnClickListener {
            navigateUpTo(intentFor<MainActivity>().singleTop())
        }
    }

    private fun initRvListener() {
        rv_history.clearOnScrollListeners()
        rv_history.addOnScrollListener(object : PaginationScrollListener(layoutManager){
            override fun loadMoreItems() {
                historyAttrib.isLoading = true
                presenter.getJobHistory(historyAttrib.currentPage+1)
            }

            override fun isLastPage(): Boolean {
                return historyAttrib.isLastPage
            }

            override fun isLoading(): Boolean {
                return historyAttrib.isLoading
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onRefresh() {
        presenter.getJobHistory()
    }
}
