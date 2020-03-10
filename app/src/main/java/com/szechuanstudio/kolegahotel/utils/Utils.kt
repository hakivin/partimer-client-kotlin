package com.szechuanstudio.kolegahotel.utils

import android.content.Context
import android.net.ParseException
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import com.szechuanstudio.kolegahotel.R
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

object Utils {

    fun convertDate(string: String?) : String? {
        val date = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && !string.isNullOrEmpty()) {
            LocalDate.parse(string, DateTimeFormatter.ISO_DATE)
        } else {
            return string
        }

        return "${date.dayOfWeek.name.toLowerCase().capitalize()}, ${date.dayOfMonth} ${date.month.name.toLowerCase().capitalize()} ${date.year}"
    }

    fun convertTime(workingDate : String?, time : String?): CharSequence? {
        val convertedTime = "$workingDate $time"
        val tm = StringTokenizer(convertedTime)
        tm.nextToken()
        val newTime = tm.nextToken()

        val sdf = SimpleDateFormat("hh:mm:ss", Locale.getDefault())
        val sdfs = SimpleDateFormat("hh:mm a", Locale.getDefault())
        val dtStart: Date
        try {
            dtStart = sdf.parse(newTime)!!
            return sdfs.format(dtStart) // <-- I got result here
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return null
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
}