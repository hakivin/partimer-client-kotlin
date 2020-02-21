package com.szechuanstudio.kolegahotel.utils

import android.content.Context
import android.os.Build
import com.szechuanstudio.kolegahotel.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object Utils {

    fun convertDate(string: String?) : String? {
        val date = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && !string.isNullOrEmpty()) {
            LocalDate.parse(string, DateTimeFormatter.ISO_DATE)
        } else {
            return string
        }

        return "${date.dayOfWeek.name.toLowerCase().capitalize()}, ${date.dayOfMonth} ${date.month.name.toLowerCase().capitalize()} ${date.year}"
    }

    fun getQuotaRemaining(quota : Int?, applied: Int?, context: Context): CharSequence? {
        return "${applied?.let { quota?.minus(it) }} ${context.getString(R.string.left)}"
    }
}