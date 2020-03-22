package com.szechuanstudio.kolegahotel.ui.main.ui.home

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
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

class HomeFragment : BaseFragment(), HomeView {

    private var currentPage: Int = PAGE_START
    private var isLastPage = false
    private var totalPage = 1
    private var isLoading = false

    private lateinit var layoutManager : LinearLayoutManager
    private lateinit var adapter: HomeAdapter
    private lateinit var presenter: HomePresenter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        val root = inflater.inflate(R.layout.fragment_home, container, false)
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
            presenter.getJobs(currentPage)
            presenter.getPositions()
            loading_home.visibility = View.VISIBLE
            layoutManager = LinearLayoutManager(context)
            rv_home_job.addOnScrollListener(object : PaginationScrollListener(layoutManager){
                override fun loadMoreItems() {
                    isLoading = true
                    presenter.getJobs(currentPage+1)
                }

                override fun isLastPage(): Boolean {
                    return isLastPage
                }

                override fun isLoading(): Boolean {
                    return isLoading
                }
            })
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
                presenter.setEmptyJob()
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                presenter.getJobs(1)
                loading_home.visibility = View.VISIBLE
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
                if (jobData.data.size >= 5)
                    adapter.addLoading()
                rv_home_job.adapter = adapter
            }
            currentPage = jobData?.current_page!!
            totalPage = jobData.last_page!!
            isLastPage = false
            isLoading = false
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
        currentPage = jobs?.current_page!!
        if (currentPage != PAGE_START) adapter.removeLoading()
        jobs.data?.let { adapter.addItems(it) }
        if (currentPage < totalPage)
            adapter.addLoading()
        else
            isLastPage = true
        isLoading = false
    }

    private fun addChipView(pos : Model.Position?){
        if (isAdded) {
            val chipGroup = act.findViewById<ChipGroup>(R.id.filter_chip_group)
            val chip =
                layoutInflater.inflate(R.layout.filter_job_chip_item, chipGroup, false) as Chip
            chip.text = pos?.nama_posisi
            chip.setOnClickListener {
                if (chip.isChecked)
                    presenter.searchJobWithPosition(pos?.id)
                else
                    presenter.getJobs(1)
            }
            filter_chip_group.addView(chip)
        }
    }
}