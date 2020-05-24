package app.mobile.consideredcosts.sign

import android.content.Context
import android.content.Intent
import android.graphics.Point
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import app.mobile.consideredcosts.R
import app.mobile.consideredcosts.basic.FieldValidator
import app.mobile.consideredcosts.data.SharedPreferencesManager
import app.mobile.consideredcosts.http.BaseResponse
import app.mobile.consideredcosts.http.RetrofitClient
import app.mobile.consideredcosts.http.models.LoginRequestResponse
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_sign.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException

class SignActivity : AppCompatActivity(), ActivityChanger {

    private var currentScreenState = SignOption.LOGIN
    private val sharedPreferencesManager by lazy { SharedPreferencesManager(this) }
    private val fieldValidator = FieldValidator()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign)

        val size = Point()
        windowManager.defaultDisplay.getSize(size)
        signScreenLayout.viewTreeObserver.addOnGlobalLayoutListener {
            if (size.y - signScreenLayout.height > 100 && currentScreenState == SignOption.REGISTRATION) {
                imageSignLogo.visibility = View.GONE
            } else {
                imageSignLogo.visibility = View.VISIBLE
            }
        }

        screenState(currentScreenState)

        emailField.setOnTouchListener { _, _ ->
            setThemeDefault(emailLabel, emailField)
            false
        }
        emailField.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) validateEmail(emailField.text.toString())
        }

        usernameField.setOnTouchListener { _, _ ->
            setThemeDefault(usernameLabel, usernameField)
            false
        }
        usernameField.setOnFocusChangeListener { _, hasFocus ->
            when (currentScreenState) {
                SignOption.LOGIN -> {
                    if (!hasFocus) validateNotEmpty(usernameLabel, usernameField)
                }
                SignOption.REGISTRATION -> {
                    if (!hasFocus) validateUsername(usernameField.text.toString())
                }
            }
        }

        passwordField.setOnTouchListener { _, _ ->
            setThemeDefault(passwordLabel, passwordField)
            false
        }
        passwordField.setOnFocusChangeListener { _, hasFocus ->
            when (currentScreenState) {
                SignOption.LOGIN -> {
                    if (!hasFocus) validateNotEmpty(passwordLabel, passwordField)
                }
                SignOption.REGISTRATION -> {
                    if (!hasFocus) validatePassword(passwordField.text.toString())
                }
            }
        }

        confPassField.setOnTouchListener { _, _ ->
            setThemeDefault(confPassLabel, confPassField)
            false
        }
        confPassField.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) validateConfPass(
                passwordField.text.toString(),
                confPassField.text.toString()
            )
        }
    }


    private fun screenState(state: SignOption) {
        imageSignLogo.visibility = View.VISIBLE
        when (state) {
            SignOption.LOGIN -> {
                resetFields(SignOption.LOGIN)
                usernameField.imeOptions = EditorInfo.IME_ACTION_NEXT
                bottomSignLink.setOnClickListener {
                    currentScreenState = SignOption.REGISTRATION
                    screenState(currentScreenState)
                }

                signButton.setOnClickListener {
                    closeKeyboard()
                    login(usernameField.text.toString(), passwordField.text.toString())
                }
            }

            SignOption.REGISTRATION -> {
                usernameField.imeOptions = EditorInfo.IME_ACTION_DONE
                resetFields(SignOption.REGISTRATION)
                bottomSignLink.setOnClickListener {
                    currentScreenState = SignOption.LOGIN
                    screenState(currentScreenState)
                }

                signButton.setOnClickListener {
                    closeKeyboard()
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
                bottomSignText.text = resources.getText(R.string.signDoNotHaveAccount)
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
            try {
                GlobalScope.launch {
                    withContext(Dispatchers.IO) {
                        launch {
                            val response =
                                RetrofitClient.registration(username, email, password, 36)
                            when (response.code()) {
                                200 -> {
                                    login(username, password)
                                }
                                504, 503, 502, 501, 500 -> {
                                    invokeGeneralErrorActivity(resources.getString(R.string.serverNotAvailable))
                                }
                                else -> {
                                    invokeGeneralErrorActivity(
                                        response.body()?.firstMessage()
                                            ?: resources.getString(R.string.unknownError)
                                    )
                                }
                            }
                        }
                    }
                }
            } catch (ex: SocketTimeoutException) {
                invokeGeneralErrorActivity(resources.getString(R.string.serverNotAvailable))

            }
        }
    }

    private fun login(username: String, password: String) {
        if (validateLogin()) {
            try {
                GlobalScope.launch {
                    withContext(Dispatchers.IO) {
                        launch {
                            val response = RetrofitClient.login(username, password)
                            when (response.code()) {
                                200 -> {
                                    sharedPreferencesManager.setPassword(password)
                                    sharedPreferencesManager.setUsername(username)
                                    sharedPreferencesManager.setToken(response.body()!!.data!!.access_token)
                                    invokePinActivity()
                                }
                                504, 503, 502, 501, 500 -> {
                                    invokeGeneralErrorActivity(resources.getString(R.string.serverNotAvailable))
                                }
                                else -> {
                                    invokeGeneralErrorActivity(resources.getString(R.string.incorrectLogin))
                                }
                            }

                        }
                    }
                }
            } catch (ex: SocketTimeoutException) {
                invokeGeneralErrorActivity(resources.getString(R.string.serverNotAvailable))
            }
        }
    }

    override fun invokePinActivity() {
        startActivity(Intent(this, PinActivity::class.java))
        finish()
    }

    override fun invokeGeneralErrorActivity(errorText: String) {
        val snackBar = Snackbar.make(
            signScreenLayout,
            errorText,
            Snackbar.LENGTH_LONG
        )

        snackBar.view.setBackgroundColor(
            ContextCompat.getColor(
                applicationContext,
                R.color.colorError
            )
        )
        snackBar.setActionTextColor(
            ContextCompat.getColor(
                applicationContext,
                R.color.colorPrimaryText
            )
        )
        snackBar.show()
    }

    private fun validateRegistration(
        username: String,
        email: String,
        password: String,
        confPass: String
    ): Boolean {
        var allFieldsValid = true
        allFieldsValid = validateUsername(username)
        allFieldsValid = validateEmail(email)
        allFieldsValid = validatePassword(password)
        allFieldsValid = validateConfPass(password, confPass)

        return allFieldsValid
    }

    private fun validateLogin(
    ): Boolean {
        var allFieldsValid = true
        allFieldsValid = validateNotEmpty(usernameLabel, usernameField)
        allFieldsValid = validateNotEmpty(passwordLabel, passwordField)

        return allFieldsValid
    }

    private fun validateNotEmpty(textView: TextView, editText: EditText): Boolean {
        if (editText.text.toString() == "") {
            editText.error = resources.getString(R.string.errorFieldIsRequired)
            setThemeError(textView, editText)
            return false
        }
        return true
    }

    private fun validateUsername(username: String): Boolean {
        if (username == "") {
            usernameField.error = resources.getString(R.string.errorFieldIsRequired)
            setThemeError(usernameLabel, usernameField)
            return false
        } else if (!fieldValidator.isUsernameValid(username)) {
            usernameField.error = resources.getString(R.string.errorUsernameField)
            setThemeError(usernameLabel, usernameField)
            return false
        }
        return true
    }

    private fun validateEmail(email: String): Boolean {
        if (email == "") {
            emailField.error = resources.getString(R.string.errorFieldIsRequired)
            setThemeError(emailLabel, emailField)
            return false
        } else if (!fieldValidator.isEmailValid(email)) {
            emailField.error = resources.getString(R.string.errorEmailField)
            setThemeError(emailLabel, emailField)
            return false
        }
        return true
    }

    private fun validatePassword(password: String): Boolean {
        if (password == "") {
            setThemeError(passwordLabel, passwordField)
            passwordField.error = resources.getString(R.string.errorFieldIsRequired)
            return false
        } else if (!fieldValidator.isPasswordValid(password)) {
            passwordField.error = resources.getString(R.string.errorPasswordField)
            setThemeError(passwordLabel, passwordField)
            return false
        }
        return true
    }

    private fun validateConfPass(password: String, confPass: String): Boolean {

        if (confPass == "") {
            confPassField.error = resources.getString(R.string.errorFieldIsRequired)
            setThemeError(confPassLabel, confPassField)
            return false
        }
        if (password != confPass) {
            confPassField.error = resources.getString(R.string.errorPassesDoNotMatch)
            setThemeError(passwordLabel, passwordField)
            setThemeError(confPassLabel, confPassField)
            return false
        }
        setThemeDefault(passwordLabel, passwordField)
        return true
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

    private fun closeKeyboard() {
        try {
            val view: View? = this.currentFocus
            view.let {
                val inputMethodManager =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(view!!.windowToken, 0)
            }
        }catch (ex:KotlinNullPointerException)
        {
            Log.e("Crash","Keyboard is already hidden crash")
        }
    }
}

enum class SignOption {
    LOGIN,
    REGISTRATION
}