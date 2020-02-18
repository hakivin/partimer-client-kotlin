package com.szechuanstudio.kolegahotel.ui.main.ui.profile.update

import com.szechuanstudio.kolegahotel.data.model.Model

interface UpdateProfileView{
    fun success()
    fun failed()
    fun getPhoto(profile: Model.Profile)
    fun getCover(profile: Model.Profile)
}