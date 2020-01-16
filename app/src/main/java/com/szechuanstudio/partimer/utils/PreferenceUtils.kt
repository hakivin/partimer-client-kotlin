package com.szechuanstudio.partimer.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

class PreferenceUtils {

    companion object {

        private fun getPreference(context: Context) : SharedPreferences {
            return PreferenceManager.getDefaultSharedPreferences(context)
        }

        fun saveEmail(email : String, context: Context) {
            getPreference(context).edit()
                .putString(Constant.KEY_EMAIL, email)
                .apply()
        }

        fun getEmail(context: Context) : String? {
            return getPreference(context).getString(Constant.KEY_EMAIL, null)
        }

        fun savePassword(password : String, context: Context){
            getPreference(context).edit()
                .putString(Constant.KEY_PASSWORD, password)
                .apply()
        }

        fun getPassword(context: Context) : String? {
            return getPreference(context).getString(Constant.KEY_PASSWORD, null)
        }

        @SuppressLint("ApplySharedPref")
        fun reset(context: Context) {
            getPreference(context).edit()
                .clear()
                .commit()
        }
    }
}