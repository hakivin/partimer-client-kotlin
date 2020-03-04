package com.szechuanstudio.kolegahotel.ui.main.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.szechuanstudio.kolegahotel.R
import com.szechuanstudio.kolegahotel.data.model.Model
import com.szechuanstudio.kolegahotel.data.retrofit.RetrofitClient
import org.jetbrains.anko.support.v4.act
import org.jetbrains.anko.support.v4.toast

class DashboardFragment : Fragment(), DashboardView {

    private lateinit var presenter: DashboardPresenter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)
        presenter = DashboardPresenter(this, RetrofitClient.getInstance(), act.applicationContext)
        presenter.getUserJob()
        return root
    }

    override fun showJobs(jobs: List<Model.JobData>?) {
        if (isAdded){
            if (jobs.isNullOrEmpty()){
                toast("No job applied")
            } else {
                toast("${jobs.size} jobs applied")
            }
        }
    }

    override fun reject() {

    }
}