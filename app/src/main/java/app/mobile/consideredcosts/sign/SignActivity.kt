package app.mobile.consideredcosts.sign

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import app.mobile.consideredcosts.R
import app.mobile.consideredcosts.basic.FieldValidator
import app.mobile.consideredcosts.data.SharedPreferencesManager
import app.mobile.consideredcosts.http.RetrofitClient
import app.mobile.consideredcosts.main.MainActivity
import kotlinx.android.synthetic.main.activity_sign.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SignActivity : AppCompatActivity(), ActivityChanger {

    private var currentScreenState = SignOption.LOGIN
    private val sharedPreferencesManager by lazy { SharedPreferencesManager(this) }
    private val fieldValidator = FieldValidator()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val defaultScreenSize = Rect()

        setContentView(R.layout.activity_sign)
        screenState(currentScreenState)


        emailField.setOnTouchListener { _, _ ->
            imageSignLogo.visibility = View.GONE
            setThemeDefault(emailLabel, emailField)
            false
        }

        usernameField.setOnTouchListener { _, _ ->
            setThemeDefault(usernameLabel, usernameField)
            imageSignLogo.visibility = View.GONE
            false
        }

        passwordField.setOnTouchListener { _, _ ->
            imageSignLogo.visibility = View.GONE
            setThemeDefault(passwordLabel, passwordField)
            false
        }

        confPassField.setOnTouchListener { _, _ ->
            imageSignLogo.visibility = View.GONE
            setThemeDefault(confPassLabel, confPassField)
            false
        }
    }

    private fun screenState(state: SignOption) {
        when (state) {
            SignOption.LOGIN -> {
                resetFields(SignOption.LOGIN)
                bottomSignLink.setOnClickListener {
                    currentScreenState = SignOption.REGISTRATION
                    screenState(currentScreenState)
                }

                signButton.setOnClickListener {
                    login(usernameField.text.toString(), passwordField.text.toString())
                }
            }


            SignOption.REGISTRATION -> {
                resetFields(SignOption.REGISTRATION)
                bottomSignLink.setOnClickListener {
                    currentScreenState = SignOption.LOGIN
                    screenState(currentScreenState)
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

    private fun resetFields(state: SignOption) {
        usernameField.text = null
        usernameField.error = null
        emailField.text = null
        emailField.error = null
        passwordField.text = null
        passwordField.error = null
        confPassField.text = null
        confPassField.error = null
        setThemeDefault(emailLabel, emailField)
        setThemeDefault(confPassLabel, confPassField)
        setThemeDefault(usernameLabel, usernameField)
        setThemeDefault(passwordLabel, passwordField)

        when (state) {
            SignOption.LOGIN -> {
                emailLabel.visibility = View.GONE
                emailField.visibility = View.GONE
                confPassLabel.visibility = View.GONE
                confPassField.visibility = View.GONE
                signButton.text = resources.getText(R.string.signInButtonText)
                bottomSignText.text = resources.getText(R.string.signDontHaveAccount)
                bottomSignLink.text = resources.getText(R.string.signUpButtonText)
            }
            SignOption.REGISTRATION -> {
                emailLabel.visibility = View.VISIBLE
                emailField.visibility = View.VISIBLE
                confPassLabel.visibility = View.VISIBLE
                confPassField.visibility = View.VISIBLE
                signButton.text = resources.getText(R.string.signUpButtonText)
                bottomSignText.text = resources.getText(R.string.signAlreadyHave)
                bottomSignLink.text = resources.getText(R.string.signInButtonText)
            }
        }
    }

    private fun registration(
        username: String,
        email: String,
        password: String,
        confirmPass: String
    ) {
        val areAllFieldsValid = validateRegistration(username, email, password, confirmPass)
        if (areAllFieldsValid) {
            GlobalScope.launch {
                withContext(Dispatchers.IO) {
                    launch {
                        val response = RetrofitClient.registration(username, email, password)
                        when (response.code()) {
                            200 -> {
                                login(username, password)
                            }
                            400 -> {
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
    }

    private fun login(username: String, password: String) {
        if (validateLogin(username, password)) {
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
        } else {

        }
    }

    override fun invokeMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun invokeGeneralErrorActivity(errorText: MutableList<String>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    private fun validateRegistration(
        username: String,
        email: String,
        password: String,
        confPass: String
    ): Boolean {
        var allFieldsValid = true
        if (username == "") {
            usernameField.error = resources.getString(R.string.errorFieldIsRequired)
            allFieldsValid = false
            setThemeError(usernameLabel, usernameField)
        } else if (!fieldValidator.isUsernameValid(username)) {
            usernameField.error = resources.getString(R.string.errorUsernameField)
            setThemeError(usernameLabel, usernameField)
        }
        if (email == "") {
            emailField.error = resources.getString(R.string.errorFieldIsRequired)
            allFieldsValid = false
            setThemeError(emailLabel, emailField)
        } else if (!fieldValidator.isEmailValid(email)) {
            emailField.error = resources.getString(R.string.errorEmailField)
            allFieldsValid = false
            setThemeError(emailLabel, emailField)
        }

        if (password == "") {
            allFieldsValid = false
            setThemeError(passwordLabel, passwordField)
            passwordField.error = resources.getString(R.string.errorFieldIsRequired)
        } else if (!fieldValidator.isPasswordValid(password)) {
            passwordField.error = resources.getString(R.string.errorPasswordField)
            confPassField.error = resources.getString(R.string.errorPasswordField)
            allFieldsValid = false
            setThemeError(passwordLabel, passwordField)
            setThemeError(confPassLabel, confPassField)
        }
        if (password != confPass) {
            confPassField.error = resources.getString(R.string.errorPassesDontMatch)
            allFieldsValid = false
            setThemeError(passwordLabel, passwordField)
            setThemeError(confPassLabel, confPassField)
        }

        if (confPass == "") {
            confPassField.error = resources.getString(R.string.errorFieldIsRequired)
            allFieldsValid = false
            setThemeError(confPassLabel, confPassField)
        }
        return allFieldsValid
    }

    private fun validateLogin(
        username: String,
        password: String
    ): Boolean {
        var allFieldsValid = true
        if (username == "") {
            usernameField.error = resources.getString(R.string.errorFieldIsRequired)
            allFieldsValid = false
            setThemeError(usernameLabel, usernameField)
        }

        if (password == "") {
            passwordField.error = resources.getString(R.string.errorFieldIsRequired)
            allFieldsValid = false
            setThemeError(passwordLabel, passwordField)
        }

        return allFieldsValid
    }

    private fun setThemeDefault(textView: TextView, editText: EditText) {
        textView.setTextColor(ContextCompat.getColor(this, R.color.colorPrimaryText))
        editText.setBackgroundResource(R.drawable.sign_rounded_edit_texts)
        editText.setTextColor(ContextCompat.getColor(this, R.color.colorPrimaryText))
    }

    private fun setThemeError(textView: TextView, editText: EditText) {
        textView.setTextColor(ContextCompat.getColor(this, R.color.colorError))
        editText.setBackgroundResource(R.drawable.sign_rounded_edit_texts_error)
        editText.setTextColor(ContextCompat.getColor(this, R.color.colorError))
    }
}


enum class SignOption {
    LOGIN,
    REGISTRATION
}