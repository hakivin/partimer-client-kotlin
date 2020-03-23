package com.szechuanstudio.kolegahotel.ui.main.ui.home

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.szechuanstudio.kolegahotel.R
import com.szechuanstudio.kolegahotel.data.model.Model
import com.szechuanstudio.kolegahotel.data.retrofit.RetrofitClient
import com.szechuanstudio.kolegahotel.utils.BaseFragment
import com.szechuanstudio.kolegahotel.utils.Constant
import com.szechuanstudio.kolegahotel.utils.PaginationScrollListener
import com.szechuanstudio.kolegahotel.utils.PaginationScrollListener.Companion.PAGE_START
import kotlinx.android.synthetic.main.fragment_home.*
import org.jetbrains.anko.support.v4.act
import org.jetbrains.anko.support.v4.toast

class HomeFragment : BaseFragment(), HomeView, SwipeRefreshLayout.OnRefreshListener {

    private lateinit var homeAttrib : Model.PageAttrib
    private lateinit var searchAttrib : Model.PageAttrib
    private lateinit var positionAttrib : Model.PageAttrib

    private lateinit var layoutManager : LinearLayoutManager

    private lateinit var adapter: HomeAdapter
    private lateinit var emptyAdapter: HomeAdapter
    private lateinit var searchedAdapter: HomeAdapter
    private lateinit var positionAdapter: HomeAdapter

    private lateinit var presenter: HomePresenter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        presenter = HomePresenter(this, RetrofitClient.getInstance(), context!!)
        return getPersistentView(inflater, container, savedInstanceState, R.layout.fragment_home)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!hasInitializedRootView) {
            hasInitializedRootView = true
            presenter.getJobs(1)
            presenter.getPositions()
            loading_home.visibility = View.VISIBLE
            layoutManager = LinearLayoutManager(context)
            emptyAdapter = HomeAdapter(ArrayList(), this, null)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.home_menu, menu)
        val search = menu.findItem(R.id.search_bar)
        val searchManager : SearchManager? = act.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        if (searchManager != null){
            val searchView = (menu.findItem(R.id.search_bar)).actionView as SearchView
            searchView.setSearchableInfo(searchManager.getSearchableInfo(act.componentName))
            searchView.queryHint = "Search"
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    presenter.searchJob(query)
                    searchView.clearFocus()
                    loading_home.visibility = View.VISIBLE
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return true
                }
            })
        }
        search.setOnActionExpandListener(object : MenuItem.OnActionExpandListener{
            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                rv_home_job.adapter = emptyAdapter
                filter_chip_group.visibility = View.GONE
                refresh_home.isEnabled = false
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                setToDefault()
                filter_chip_group.visibility = View.VISIBLE
                return true
            }

        })
    }

    override fun showAllJobs(jobData: Model.JobPaginate?) {
        if (isAdded) {
            rv_home_job.layoutManager = layoutManager
            if (jobData?.data != null) {
                if(jobData.data.isNullOrEmpty())
                    toast("No result")
                adapter = HomeAdapter(jobData.data as ArrayList<Model.JobData>, this, null)
                if (jobData.current_page!! < jobData.last_page!!)
                    adapter.addLoading()
                rv_home_job.adapter = adapter
            }
            homeAttrib = Model.PageAttrib(jobData?.current_page!!, false, jobData.last_page!!, false)
            refresh_home.setOnRefreshListener(this)
            setToDefault()
            loading_home.visibility = View.GONE
            refresh_home.isRefreshing = false
        }
    }

    override fun showSearchedJobs(jobs: Model.JobPaginate?, query: String?) {
        if (isAdded){
            if (jobs?.data != null) {
                searchedAdapter = HomeAdapter(jobs.data as ArrayList<Model.JobData>, this, null)
                if (jobs.current_page!! < jobs.last_page!!)
                    searchedAdapter.addLoading()
                rv_home_job.adapter = searchedAdapter
            }
            searchAttrib = Model.PageAttrib(jobs?.current_page!!, false, jobs.last_page!!, false)

            rv_home_job.clearOnScrollListeners()
            rv_home_job.addOnScrollListener(object : PaginationScrollListener(layoutManager){
                override fun loadMoreItems() {
                    searchAttrib.isLoading = true
                    presenter.searchJob(query, searchAttrib.currentPage +1)
                }

                override fun isLastPage(): Boolean {
                    return searchAttrib.isLastPage
                }

                override fun isLoading(): Boolean {
                    return searchAttrib.isLoading
                }
            })
            refresh_home.isEnabled = false
            loading_home.visibility = View.GONE
        }
    }

    override fun showPositionJobs(jobs: Model.JobPaginate?, id: Int?) {
        if (isAdded){
            if (jobs?.data != null) {
                positionAdapter = HomeAdapter(jobs.data as ArrayList<Model.JobData>, this, null)
                if (jobs.current_page!! < jobs.last_page!!)
                    positionAdapter.addLoading()
                rv_home_job.adapter = positionAdapter
            }
            positionAttrib = Model.PageAttrib(jobs?.current_page!!, false, jobs.last_page!!, false)

            rv_home_job.clearOnScrollListeners()
            rv_home_job.addOnScrollListener(object : PaginationScrollListener(layoutManager){
                override fun loadMoreItems() {
                    positionAttrib.isLoading = true
                    presenter.searchJobWithPosition(id, positionAttrib.currentPage +1)
                }

                override fun isLastPage(): Boolean {
                    return positionAttrib.isLastPage
                }

                override fun isLoading(): Boolean {
                    return positionAttrib.isLoading
                }
            })
            refresh_home.isEnabled = false
            loading_home.visibility = View.GONE
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Constant.APPLY_REQUEST_CODE){
            loading_home.visibility = View.VISIBLE
            presenter.getJobs(1)
        }
    }

    override fun reject(message: String?) {
        if (isAdded) {
            message?.let { toast(it) }
            loading_home.visibility = View.GONE
        }
    }

    override fun showPositions(positions: List<Model.Position>?) {
        if (positions != null) {
            for (pos in positions){
                addChipView(pos)
            }
        }
    }

    override fun addJobs(jobs: Model.JobPaginate?) {
        homeAttrib.currentPage = jobs?.current_page!!
        if (homeAttrib.currentPage != PAGE_START) adapter.removeLoading()
        jobs.data?.let { adapter.addItems(it) }
        if (homeAttrib.currentPage < homeAttrib.totalPage)
            adapter.addLoading()
        else
            homeAttrib.isLastPage = true
        homeAttrib.isLoading = false
    }

    override fun addSearchedJobs(jobs: Model.JobPaginate?, query: String?) {
        searchAttrib.currentPage = jobs?.current_page!!
        if (searchAttrib.currentPage != PAGE_START) searchedAdapter.removeLoading()
        jobs.data?.let { searchedAdapter.addItems(it) }
        if (searchAttrib.currentPage < searchAttrib.totalPage)
            searchedAdapter.addLoading()
        else
            searchAttrib.isLastPage = true
        searchAttrib.isLoading = false
    }

    override fun addPositionJobs(jobs: Model.JobPaginate?, id: Int?) {
        positionAttrib.currentPage = jobs?.current_page!!
        if (positionAttrib.currentPage != PAGE_START) positionAdapter.removeLoading()
        jobs.data?.let { positionAdapter.addItems(it) }
        if (positionAttrib.currentPage < positionAttrib.totalPage)
            positionAdapter.addLoading()
        else
            positionAttrib.isLastPage = true
        positionAttrib.isLoading = false
    }

    override fun showPendingJobs(jobs: Model.JobPaginate?) {
        //nothing
    }

    override fun addPendingJobs(jobs: Model.JobPaginate?) {
        //nothing
    }

    private fun addChipView(pos : Model.Position?){
        if (isAdded) {
            val chipGroup = act.findViewById<ChipGroup>(R.id.filter_chip_group)
            val chip =
                layoutInflater.inflate(R.layout.filter_job_chip_item, chipGroup, false) as Chip
            chip.text = pos?.nama_posisi
            chip.setOnClickListener {
                if (chip.isChecked) {
                    presenter.searchJobWithPosition(pos?.id)
                    rv_home_job.adapter = emptyAdapter
                    loading_home.visibility = View.VISIBLE
                }
                else
                    setToDefault()
            }
            filter_chip_group.addView(chip)
        }
    }

    private fun setToDefault(){
        rv_home_job.adapter = adapter
        refresh_home.isEnabled = true
        rv_home_job.clearOnScrollListeners()
        rv_home_job.addOnScrollListener(object : PaginationScrollListener(layoutManager){
            override fun loadMoreItems() {
                homeAttrib.isLoading = true
                presenter.getJobs(homeAttrib.currentPage+1)
            }

            override fun isLastPage(): Boolean {
                return homeAttrib.isLastPage
            }

            override fun isLoading(): Boolean {
                return homeAttrib.isLoading
            }
        })
    }

    override fun onRefresh() {
        presenter.getJobs(1)
        presenter.getPositions()
    }
}