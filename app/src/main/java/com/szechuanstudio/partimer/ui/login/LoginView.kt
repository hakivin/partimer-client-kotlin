package com.szechuanstudio.partimer.ui.login

import com.szechuanstudio.partimer.data.model.Model

interface LoginView {
    fun getUser(user : Model.User?)
    fun failed(message : String?)
}