package com.szechuanstudio.kolegahotel.ui.job

interface JobDetailView {
    fun reject(message: String?)
    fun success()
}