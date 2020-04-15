package app.mobile.consideredcosts.data

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesManager(context: Context) {
    private val sharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences("", Context.MODE_PRIVATE)
    }

    fun onLaunch(firstLaunch: Boolean) {
        sharedPreferences.edit().putBoolean(FIRST_OPEN, firstLaunch).apply()
    }

    fun isFirstOpened(): Boolean = sharedPreferences.getBoolean(FIRST_OPEN, false)

    fun setToken(token:String)
    {
            sharedPreferences.edit().putString(TOKEN,token).apply()
    }

    fun getToken(): String? = "Bearer " + sharedPreferences.getString(TOKEN,"No token")

    fun setUsername(username:String)
    {
        sharedPreferences.edit().putString(USERNAME,username).apply()
    }

    fun getUsername(): String? = sharedPreferences.getString(USERNAME,"No username")

    fun setPassword(password:String)
    {
        sharedPreferences.edit().putString(PASSWORD,password).apply()
    }

    fun getPassword(): String? = sharedPreferences.getString(PASSWORD,"No password")

    companion object {
        private const val FIRST_OPEN = "FIRST_OPEN"
        private const val TOKEN = "TOKEN"
        private const val USERNAME = "USERNAME"
        private const val PASSWORD = "PASSWORD"
    }
}