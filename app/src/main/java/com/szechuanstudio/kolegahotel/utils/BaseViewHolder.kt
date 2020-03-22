package com.szechuanstudio.kolegahotel.utils

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private var currentPosition : Int = 0

    abstract fun clear()

    fun onBind(position: Int){
        currentPosition = position
        clear()
    }

    fun getCurrentPosition() : Int{
        return currentPosition
    }
}