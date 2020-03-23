package com.szechuanstudio.kolegahotel.ui.main.ui.home

import com.szechuanstudio.kolegahotel.data.model.Model

interface HomeView {
    fun showAllJobs(jobData: Model.JobPaginate?)
    fun reject(message: String?)
    fun showPositions(positions: List<Model.Position>?)
    fun addJobs(jobs: Model.JobPaginate?)
    fun showSearchedJobs(jobs: Model.JobPaginate?, query: String?)
    fun addSearchedJobs(jobs: Model.JobPaginate?, query: String?)
    fun showPositionJobs(jobs: Model.JobPaginate?, id: Int?)
    fun addPositionJobs(jobs: Model.JobPaginate?, id: Int?)
    fun showPendingJobs(jobs: Model.JobPaginate?)
    fun addPendingJobs(jobs: Model.JobPaginate?)
}