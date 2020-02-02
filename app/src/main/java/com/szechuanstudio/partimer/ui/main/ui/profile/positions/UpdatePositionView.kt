package com.szechuanstudio.partimer.ui.main.ui.profile.positions

import com.szechuanstudio.partimer.data.model.Model

interface UpdatePositionView {
    fun showAllPositions(positions: List<Model.Position>?)
    fun populateUserPositions(userPositions: List<Model.Position>?)
}