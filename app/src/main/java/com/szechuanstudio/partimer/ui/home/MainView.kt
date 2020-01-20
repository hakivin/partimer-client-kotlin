package com.szechuanstudio.partimer.ui.home

import com.szechuanstudio.partimer.data.model.Model

interface MainView {
    fun showJobs()
    fun reject(profile : Model.Profile)
    fun error(message: String)
}