package com.szechuanstudio.kolegahotel.ui.main.ui.profile

import com.szechuanstudio.kolegahotel.data.model.Model

interface ProfileView {
    fun showProfile(profile : Model.Profile?)
    fun reject(message: String?)
    fun logoutSuccess()
    fun logoutFailed()
    fun showPositions(positions: Model.PositionsResponse?)
}