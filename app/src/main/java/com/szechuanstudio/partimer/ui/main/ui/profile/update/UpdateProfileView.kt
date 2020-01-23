package com.szechuanstudio.partimer.ui.main.ui.profile.update

import com.szechuanstudio.partimer.data.model.Model

interface UpdateProfileView{
    fun success()
    fun failed()
    fun getPhoto(profile: Model.Profile)
}