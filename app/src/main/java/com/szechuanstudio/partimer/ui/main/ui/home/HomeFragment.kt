package com.szechuanstudio.partimer.ui.main.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
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
        return inflater.inflate(R.layout.fragment_home, container, false)
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
}