package com.szechuanstudio.kolegahotel.ui.login

import com.szechuanstudio.kolegahotel.data.model.Model

interface LoginView {
    fun getUser(user : Model.User?)
    fun failed(message : String?)
}