package app.mobile.consideredcosts.sign

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import app.mobile.consideredcosts.R
import app.mobile.consideredcosts.http.RetrofitClient
import app.mobile.consideredcosts.http.models.request.LoginRequest
import kotlinx.android.synthetic.main.activity_sign.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SignActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign)

        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                val response = RetrofitClient.api.login(LoginRequest("Admin", "Admin"))
                when{
                    response.isSuccessful -> {
                        withContext(Dispatchers.Main)
                        {
                            signButton.text = response.body()!!.data!!.username
                        }
                    }
                }
            }
        }

    }
}
