package com.szechuanstudio.partimer.ui.main.ui.home

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.szechuanstudio.partimer.R
import com.szechuanstudio.partimer.data.model.Model
import com.szechuanstudio.partimer.data.retrofit.RetrofitClient
import kotlinx.android.synthetic.main.fragment_home.*
import org.jetbrains.anko.support.v4.act
import org.jetbrains.anko.support.v4.toast

class HomeFragment : Fragment(), HomeView {

    private lateinit var presenter: HomePresenter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        presenter = HomePresenter(this, RetrofitClient.getInstance(), act.applicationContext)
        presenter.getJobs()
        presenter.getPositions()
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
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
                presenter.getJobs()
                return true
            }

        })
    }

    override fun showAllJobs(jobs: List<Model.Job>?) {
        if (isAdded) {
            rv_home_job.layoutManager = LinearLayoutManager(context)
            if (jobs != null)
                rv_home_job.adapter = HomeAdapter(jobs)
        }
    }

    override fun reject(message: String?) {
        if (isAdded)
            message?.let { toast(it) }
    }

    override fun showPositions(positions: List<Model.Position>?) {
        if (positions != null) {
            for (pos in positions){
                addChipView(pos)
            }
        }
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
                    presenter.getJobs()
            }
            filter_chip_group.addView(chip)
        }
    }
}