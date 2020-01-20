package com.szechuanstudio.partimer.ui.profile

import com.szechuanstudio.partimer.data.model.Model

interface ProfileView {
    fun showProfile(profile : Model.Profile)
    fun reject()
}