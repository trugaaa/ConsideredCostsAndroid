package app.mobile.consideredcosts.sign

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import app.mobile.consideredcosts.R
import app.mobile.consideredcosts.data.SharedPreferencesManager
import app.mobile.consideredcosts.http.RetrofitClient
import app.mobile.consideredcosts.main.MainActivity
import kotlinx.android.synthetic.main.activity_sign.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SignActivity : AppCompatActivity(),ActivityChanger {

    private var state = SignOption.LOGIN
    private val sharedPreferencesManager by lazy { SharedPreferencesManager(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign)
        screenState(state)

    }

    private fun screenState(state: SignOption) {
        when (state) {
            SignOption.LOGIN -> {
                emailLabel.visibility = View.GONE
                emailField.visibility = View.GONE
                confPassLabel.visibility = View.GONE
                confPassField.visibility = View.GONE
                signButton.text = resources.getText(R.string.signInButtonText)

                bottomSignLink.setOnClickListener {
                    screenState(SignOption.REGISTRATION)
                }

                signButton.setOnClickListener {
                    login(usernameField.text.toString(), passwordField.text.toString())
                }
            }

            SignOption.REGISTRATION -> {
                emailLabel.visibility = View.VISIBLE
                emailField.visibility = View.VISIBLE
                confPassLabel.visibility = View.VISIBLE
                confPassField.visibility = View.VISIBLE
                signButton.text = resources.getText(R.string.signUpButtonText)

                bottomSignLink.setOnClickListener {
                    screenState(SignOption.LOGIN)
                }

                signButton.setOnClickListener {
                    //todo Имплементация
                }
            }
        }
    }

    private fun registration(
        username: String,
        email: String,
        password: String,
        confirmPass: String
    ) {
      //todo Имплементация
    }

    private fun login(username: String, password: String) {
        if (username.length < 3) {
            //todo Сделать обработку пустого
        }

        if (password.length < 3) {
            //todo Сделать обработку пустого
        }

        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                launch {
                    val response = RetrofitClient.get(username, password)
                    when (response.code()) {
                        200 -> {
                            sharedPreferencesManager.setPassword(password)
                            sharedPreferencesManager.setUsername(username)
                            sharedPreferencesManager.setToken(response.body()!!.data!!.access_token)
                            invokeMainActivity()
                        }
                        401 -> {
                            //todo Сделать обработку
                            withContext(Dispatchers.Main) {
                                Toast.makeText(
                                    this@SignActivity,
                                    response.body()!!.message,
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                        }
                        else -> {
                            //todo Сделать обработку
                            withContext(Dispatchers.Main) {
                                Toast.makeText(this@SignActivity, "Else Pedik", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    }

                }
            }
        }
}
    override fun invokeMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()    }

    override fun invokeGeneralErrorActivity() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

enum class SignOption {
    LOGIN,
    REGISTRATION
}