package com.szechuanstudio.kolegahotel.ui.register

import com.szechuanstudio.kolegahotel.data.model.Model

interface RegisterView {
    fun registered(user: Model.User?)
    fun failed(message : String?)
}