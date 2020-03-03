package com.szechuanstudio.kolegahotel.utils

import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.TextView
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

    fun getRealPathFromURI(context: Context, contentURI: Uri?): String? {
        val result: String?
        val cursor =
            context.contentResolver.query(contentURI!!, null, null, null, null)
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.path
        } else {
            cursor.moveToFirst()
            val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            result = cursor.getString(idx)
            cursor.close()
        }
        return result
    }

    fun setTextViewColor(tv : TextView, color: Int, context: Context){
        tv.setTextColor(color)
    }
}