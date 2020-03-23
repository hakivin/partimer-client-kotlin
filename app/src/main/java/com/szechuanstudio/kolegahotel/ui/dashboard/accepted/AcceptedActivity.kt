package com.szechuanstudio.kolegahotel.ui.dashboard.accepted

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.szechuanstudio.kolegahotel.R
import com.szechuanstudio.kolegahotel.data.model.Model
import com.szechuanstudio.kolegahotel.data.retrofit.RetrofitClient
import com.szechuanstudio.kolegahotel.ui.main.MainActivity
import com.szechuanstudio.kolegahotel.utils.PaginationScrollListener
import com.szechuanstudio.kolegahotel.utils.PaginationScrollListener.Companion.PAGE_START
import com.szechuanstudio.kolegahotel.utils.Utils
import kotlinx.android.synthetic.main.activity_accepted.*
import kotlinx.android.synthetic.main.empty_state.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.singleTop
import org.jetbrains.anko.toast

class AcceptedActivity : AppCompatActivity(), AcceptedView, SwipeRefreshLayout.OnRefreshListener {

    private lateinit var presenter: AcceptedPresenter
    private lateinit var adapter: AcceptedAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var acceptedAttrib: Model.PageAttrib

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accepted)
        presenter = AcceptedPresenter(this, RetrofitClient.getInstance(), applicationContext)
        loadContent()
        initToolbar()
        layoutManager = LinearLayoutManager(this)
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
//        message?.let { toast(it) }
        loading_accepted.visibility = View.GONE
        Utils.setOfflineState(this, this)
    }

    override fun showAcceptedJobs(jobs: Model.AcceptedPaginate?) {
        setContentView(R.layout.activity_accepted)
        if (jobs?.data != null) {
            if (!jobs.data.isNullOrEmpty()) {
                adapter = AcceptedAdapter(jobs.data as ArrayList<Model.JobAccepted>, supportFragmentManager)
                if (jobs.current_page!! < jobs.last_page!!)
                    adapter.addLoading()
                rv_accepted.layoutManager = layoutManager
                rv_accepted.adapter = adapter
                initRvListener()
                refresh_accepted.setOnRefreshListener(this)
                acceptedAttrib = Model.PageAttrib(jobs.current_page, false, jobs.last_page, false)
                loading_accepted.visibility = View.GONE
                refresh_accepted.isRefreshing = false
            } else
                setEmptyState()
        }
    }

    override fun addAcceptedJobs(jobs: Model.AcceptedPaginate?) {
        acceptedAttrib.currentPage = jobs?.current_page!!
        if (acceptedAttrib.currentPage != PAGE_START) adapter.removeLoading()
        jobs.data?.let { adapter.addItems(it) }
        if (acceptedAttrib.currentPage < acceptedAttrib.totalPage)
            adapter.addLoading()
        else
            acceptedAttrib.isLastPage = true
        acceptedAttrib.isLoading = false
    }

    private fun setEmptyState(){
        setContentView(R.layout.empty_state)
        find_job.setOnClickListener {
            navigateUpTo(intentFor<MainActivity>().singleTop())
        }
    }

    private fun initRvListener(){
        rv_accepted.clearOnScrollListeners()
        rv_accepted.addOnScrollListener(object : PaginationScrollListener(layoutManager){
            override fun loadMoreItems() {
                acceptedAttrib.isLoading = true
                presenter.getAcceptedJobs(acceptedAttrib.currentPage+1)
            }

            override fun isLastPage(): Boolean {
                return acceptedAttrib.isLastPage
            }

            override fun isLoading(): Boolean {
                return acceptedAttrib.isLoading
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onRefresh() {
        presenter.getAcceptedJobs()
    }
}
