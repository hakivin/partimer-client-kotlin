package com.szechuanstudio.kolegahotel.ui.main.ui.home

import com.szechuanstudio.kolegahotel.data.model.Model

interface HomeView {
    fun showAllJobs(jobData: Model.JobPaginate?)
    fun reject(message: String?)
    fun showPositions(positions: List<Model.Position>?)
    fun addJobs(jobs: Model.JobPaginate?)
}