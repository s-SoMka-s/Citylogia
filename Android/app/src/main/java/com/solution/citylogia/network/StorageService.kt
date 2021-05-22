package com.solution.citylogia.network

import android.content.Context
import android.content.SharedPreferences
import com.solution.citylogia.R

class StorageService constructor(val context: Context) {

    private var prefs: SharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)

    fun putItem(key: String, value: String) {
        val editor = prefs.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getItem(key: String): String? {
        return prefs.getString(key, null)
    }

    fun removeItem(key: String) {
        prefs.edit()
             .remove(key)
             .apply()
    }
}