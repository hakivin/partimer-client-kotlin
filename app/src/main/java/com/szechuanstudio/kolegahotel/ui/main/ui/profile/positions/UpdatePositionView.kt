package com.szechuanstudio.kolegahotel.ui.main.ui.profile.positions

import com.szechuanstudio.kolegahotel.data.model.Model

interface UpdatePositionView {
    fun showAllPositions(positions: List<Model.Position>?)
    fun populateUserPositions(userPositions: List<Model.Position>?)
}