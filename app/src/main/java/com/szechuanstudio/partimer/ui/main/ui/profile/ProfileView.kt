package com.szechuanstudio.partimer.ui.main.ui.profile

import com.szechuanstudio.partimer.data.model.Model

interface ProfileView {
    fun showProfile(profile : Model.Profile?)
    fun reject(message: String?)
    fun logoutSuccess()
    fun logoutFailed()
    fun showPositions(positions: Model.PositionsResponse?)
}