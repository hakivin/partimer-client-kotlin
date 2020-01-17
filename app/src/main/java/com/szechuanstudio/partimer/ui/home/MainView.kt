package com.szechuanstudio.partimer.ui.home

interface MainView {
    fun showJobs()
    fun reject()
    fun error(message: String)
}