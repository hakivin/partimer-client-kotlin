package com.szechuanstudio.kolegahotel.ui.job

interface JobDetailView {
    fun error(message: String?)
    fun reject()
    fun success()
}