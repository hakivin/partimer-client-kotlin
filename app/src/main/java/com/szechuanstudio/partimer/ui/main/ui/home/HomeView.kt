package com.szechuanstudio.partimer.ui.main.ui.home

import com.szechuanstudio.partimer.data.model.Model

interface HomeView {
    fun showAllJobs(jobs: List<Model.Job>?)
    fun reject(message: String?)
}