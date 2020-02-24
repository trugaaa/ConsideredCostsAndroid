package app.mobile.consideredcosts.data

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesManager(context: Context) {
    private val sharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences("Vasya", Context.MODE_PRIVATE)
    }

    var firstLaunch: Boolean? = null

    fun onLaunch(firstLaunch: Boolean) {
        sharedPreferences.edit().putBoolean(FIRST_OPEN, firstLaunch).apply()
    }

    fun isFirstOpened(): Boolean = sharedPreferences.getBoolean(FIRST_OPEN, false)

    companion object {
        private const val FIRST_OPEN = "FIRST_OPEN"
    }
}