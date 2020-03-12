package com.szechuanstudio.kolegahotel.ui.dashboard.accepted

import com.szechuanstudio.kolegahotel.data.model.Model

interface AcceptedView {
    fun reject(message: String?)
    fun showAcceptedJobs(jobs: List<Model.JobAccepted>?)
}