package com.szechuanstudio.kolegahotel.ui.main.ui.dashboard

import com.szechuanstudio.kolegahotel.data.model.Model

interface DashboardView {
    fun showJobs(jobs: List<Model.JobData>?)
    fun reject()
}