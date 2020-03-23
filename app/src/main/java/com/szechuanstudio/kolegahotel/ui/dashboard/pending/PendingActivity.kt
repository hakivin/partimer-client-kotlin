package com.szechuanstudio.kolegahotel.ui.dashboard.pending

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.szechuanstudio.kolegahotel.R
import com.szechuanstudio.kolegahotel.data.model.Model
import com.szechuanstudio.kolegahotel.data.retrofit.RetrofitClient
import com.szechuanstudio.kolegahotel.ui.main.MainActivity
import com.szechuanstudio.kolegahotel.ui.main.ui.home.HomeAdapter
import com.szechuanstudio.kolegahotel.ui.main.ui.home.HomePresenter
import com.szechuanstudio.kolegahotel.ui.main.ui.home.HomeView
import com.szechuanstudio.kolegahotel.utils.Constant
import com.szechuanstudio.kolegahotel.utils.PaginationScrollListener
import com.szechuanstudio.kolegahotel.utils.Utils
import kotlinx.android.synthetic.main.activity_pending.*
import kotlinx.android.synthetic.main.empty_state.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.singleTop
import org.jetbrains.anko.toast

class PendingActivity : AppCompatActivity(), HomeView, SwipeRefreshLayout.OnRefreshListener {

    private lateinit var presenter: HomePresenter
    private lateinit var adapter: HomeAdapter
    private lateinit var pendingAttrib: Model.PageAttrib

    private lateinit var layoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pending)
        presenter = HomePresenter(this, RetrofitClient.getInstance(), applicationContext)
        layoutManager = LinearLayoutManager(this)
        loadContent()
        initToolbar()
    }

    private fun initToolbar(){
        supportActionBar?.title = "Pending Jobs"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    override fun showPendingJobs(jobs: Model.JobPaginate?) {
        setContentView(R.layout.activity_pending)
        if (jobs != null) {
            if (!jobs.data.isNullOrEmpty()) {
                adapter = HomeAdapter(jobs.data as ArrayList<Model.JobData>, null, this)
                if (jobs.current_page!! < jobs.last_page!!)
                    adapter.addLoading()
                rv_pending.layoutManager = layoutManager
                rv_pending.adapter = adapter
                initData()
                pendingAttrib = Model.PageAttrib(jobs.current_page, false, jobs.last_page, false)
                refresh_pending.setOnRefreshListener(this)
                loading_pending.visibility = View.GONE
                refresh_pending.isRefreshing = false
            } else
                setEmptyState()
        }
    }

    override fun addPendingJobs(jobs: Model.JobPaginate?) {
        pendingAttrib.currentPage = jobs?.current_page!!
        if (pendingAttrib.currentPage != PaginationScrollListener.PAGE_START) adapter.removeLoading()
        jobs.data?.let { adapter.addItems(it) }
        if (pendingAttrib.currentPage < pendingAttrib.totalPage)
            adapter.addLoading()
        else
            pendingAttrib.isLastPage = true
        pendingAttrib.isLoading = false
    }

    private fun setEmptyState(){
        setContentView(R.layout.empty_state)
        find_job.setOnClickListener {
            navigateUpTo(intentFor<MainActivity>().singleTop())
        }
    }

    override fun reject(message: String?) {
//        message?.let { toast(it) }
        loading_pending.visibility = View.GONE
        refresh_pending.isRefreshing = false
        Utils.setOfflineState(this, this)
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

    private fun initData(){
        rv_pending.clearOnScrollListeners()
        rv_pending.addOnScrollListener(object : PaginationScrollListener(layoutManager){
            override fun loadMoreItems() {
                pendingAttrib.isLoading = true
                presenter.getPendingJobs(pendingAttrib.currentPage+1)
            }

            override fun isLastPage(): Boolean {
                return pendingAttrib.isLastPage
            }

            override fun isLoading(): Boolean {
                return pendingAttrib.isLoading
            }
        })
    }

    override fun onRefresh() {
        loadContent()
    }

    override fun showAllJobs(jobData: Model.JobPaginate?) {
        //nothing
    }

    override fun showPositions(positions: List<Model.Position>?) {
        // nothing to do
    }

    override fun addJobs(jobs: Model.JobPaginate?) {
        //nothing
    }

    override fun showSearchedJobs(jobs: Model.JobPaginate?, query: String?) {
        //nothing
    }

    override fun addSearchedJobs(jobs: Model.JobPaginate?, query: String?) {
        //nothing
    }

    override fun showPositionJobs(jobs: Model.JobPaginate?, id: Int?) {
        //nothing
    }

    override fun addPositionJobs(jobs: Model.JobPaginate?, id: Int?) {
        //nothing
    }
}
