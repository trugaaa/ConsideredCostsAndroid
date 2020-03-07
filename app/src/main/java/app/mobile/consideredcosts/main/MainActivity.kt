package app.mobile.consideredcosts.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import app.mobile.consideredcosts.R
import app.mobile.consideredcosts.data.SharedPreferencesManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val sharedPreferencesManager by lazy { SharedPreferencesManager(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val text: String =
            "Your username " + sharedPreferencesManager.getUsername() + "\n" + "Your password " + sharedPreferencesManager.getPassword() + "\n" + "Token " + sharedPreferencesManager.getToken()
        random.text = text
    }
}
