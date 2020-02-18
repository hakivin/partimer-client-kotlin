package com.szechuanstudio.kolegahotel.ui.main.ui.home

import com.szechuanstudio.kolegahotel.data.model.Model

interface HomeView {
    fun showAllJobs(jobs: List<Model.Job>?)
    fun reject(message: String?)
    fun showPositions(positions: List<Model.Position>?)
}