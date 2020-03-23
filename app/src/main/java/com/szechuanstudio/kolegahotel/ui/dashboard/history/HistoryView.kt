package com.szechuanstudio.kolegahotel.ui.dashboard.history

import com.szechuanstudio.kolegahotel.data.model.Model

interface HistoryView {
    fun reject(message: String)
    fun showJobHistory(jobs: Model.HistoryPaginate?)
    fun addJobHistory(jobs: Model.HistoryPaginate?)
}