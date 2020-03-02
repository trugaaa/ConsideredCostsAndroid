package app.mobile.consideredcosts.sign

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import app.mobile.consideredcosts.R
import kotlinx.android.synthetic.main.activity_sign.*

class SignActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign)

        emailField.visibility = View.INVISIBLE
    }
}
