package com.szechuanstudio.partimer.ui.register

import com.szechuanstudio.partimer.data.model.Model

interface RegisterView {
    fun registered(user: Model.User?)
    fun failed(message : String?)
}