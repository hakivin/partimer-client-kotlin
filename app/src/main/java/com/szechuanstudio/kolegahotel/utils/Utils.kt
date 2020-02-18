package com.szechuanstudio.kolegahotel.utils

import android.os.Build
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object Utils {

    fun convertDate(string: String?) : String? {
        val date = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && !string.isNullOrEmpty()) {
            LocalDate.parse(string, DateTimeFormatter.ISO_DATE)
        } else {
            return string
        }

        return "${date.dayOfMonth} ${date.month.name.toLowerCase().capitalize()} ${date.year}"
    }
}