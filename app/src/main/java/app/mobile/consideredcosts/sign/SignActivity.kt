package app.mobile.consideredcosts.sign

import android.content.Intent
import android.os.Bundle
import android.view.View
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

class SignActivity : AppCompatActivity(), ActivityChanger {

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
                usernameField.text = null
                emailField.text = null
                passwordField.text = null
                confPassField.text = null

                emailLabel.visibility = View.GONE
                emailField.visibility = View.GONE
                confPassLabel.visibility = View.GONE
                confPassField.visibility = View.GONE
                signButton.text = resources.getText(R.string.signInButtonText)
                bottomSignText.text = resources.getText(R.string.signDontHaveAccount)
                bottomSignLink.text = resources.getText(R.string.signUpButtonText)

                bottomSignLink.setOnClickListener {
                    screenState(SignOption.REGISTRATION)
                }

                signButton.setOnClickListener {
                    login(usernameField.text.toString(), passwordField.text.toString())
                }
            }

            SignOption.REGISTRATION -> {
                usernameField.text = null
                emailField.text = null
                passwordField.text = null
                confPassField.text = null

                emailLabel.visibility = View.VISIBLE
                emailField.visibility = View.VISIBLE
                confPassLabel.visibility = View.VISIBLE
                confPassField.visibility = View.VISIBLE
                signButton.text = resources.getText(R.string.signUpButtonText)
                bottomSignText.text = resources.getText(R.string.signAlreadyHave)
                bottomSignLink.text = resources.getText(R.string.signInButtonText)

                bottomSignLink.setOnClickListener {
                    screenState(SignOption.LOGIN)
                }

                signButton.setOnClickListener {
                    registration(
                        usernameField.text.toString(),
                        emailField.text.toString(),
                        passwordField.text.toString(),
                        confPassField.text.toString()
                    )
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
        //todo implementation
    }

    private fun login(username: String, password: String) {
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                launch {
                    val response = RetrofitClient.login(username, password)
                    when (response.code()) {
                        200 -> {
                            sharedPreferencesManager.setPassword(password)
                            sharedPreferencesManager.setUsername(username)
                            sharedPreferencesManager.setToken(response.body()!!.data!!.access_token)
                            invokeMainActivity()
                        }
                        401 -> {
                            //todo Сделать обработку
                        }
                        else -> {
                            //todo Сделать обработку
                        }
                    }

                }
            }
        }
    }

    override fun invokeMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun invokeGeneralErrorActivity() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

enum class SignOption {
    LOGIN,
    REGISTRATION
}