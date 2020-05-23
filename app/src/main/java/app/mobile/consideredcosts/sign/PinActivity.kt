package app.mobile.consideredcosts.sign

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.content.ContextCompat
import app.mobile.consideredcosts.R
import app.mobile.consideredcosts.data.SharedPreferencesManager
import app.mobile.consideredcosts.http.RetrofitClient
import app.mobile.consideredcosts.main.MainActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_pin.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.net.SocketTimeoutException

class PinActivity : AppCompatActivity() {
    private lateinit var currentState: PinScreenState
    private val sharedPreferencesManager by lazy { SharedPreferencesManager(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pin)
        if (!sharedPreferencesManager.isPinDataActual()) {
            invokeSignActivity()
        }

        logout.setOnClickListener {
            sharedPreferencesManager.clearLoginData()
            invokeSignActivity()
        }
    }

    override fun onResume() {
        super.onResume()
        if (sharedPreferencesManager.getIsPinSet()!!) {
            currentState = PinScreenState.ENTER
            pinConfirmText.visibility = View.GONE
            pinText.imeOptions = EditorInfo.IME_ACTION_DONE
        } else {
            currentState = PinScreenState.SETUP
            pinText.imeOptions = EditorInfo.IME_ACTION_NEXT
            pinConfirmText.visibility = View.VISIBLE
        }

        pinText.setOnClickListener {
            setThemeDefault(pinText)
        }

        pinConfirmText.setOnClickListener {
            setThemeDefault(pinConfirmText)
        }

        submitPinButton.setOnClickListener {
            try {
                closeKeyboard()
            } catch (ex: Exception) {
                ex.message.let { Log.e("Crash", ex.message!!) }
            }

            if (sharedPreferencesManager.isPinDataActual()) {
                if (currentState == PinScreenState.SETUP) {
                    if (validatePinFields()) {
                        sharedPreferencesManager.setPin(pinConfirmText.text.toString())
                        login()
                    }
                } else {
                    if (validatePinEnter()) {
                        if (sharedPreferencesManager.getPin() == pinText.text.toString()) {
                            login()
                        } else {
                            pinText.text.clear()
                            invokeGeneralErrorActivity(resources.getString(R.string.pinIncorrect))
                        }
                    }
                }
            } else {
                invokeSignActivity()
            }
        }
    }

    private fun login() {
        try {
            GlobalScope.launch {
                withContext(Dispatchers.IO) {
                    launch {
                        val response = RetrofitClient.login(
                            sharedPreferencesManager.getUsername()!!,
                            sharedPreferencesManager.getPassword()!!
                        )
                        when (response.code()) {
                            200 -> {
                                sharedPreferencesManager.setToken(response.body()!!.data!!.access_token)
                                sharedPreferencesManager.setIsPinSet(true)
                                invokeMainActivity()
                            }
                            504, 503, 502, 501, 500 -> {
                                invokeGeneralErrorActivity(resources.getString(R.string.serverNotAvailable))
                            }
                            401 -> {
                                invokeSignActivity()
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

    private fun validatePinFields(): Boolean {
        val pin = pinText.text.toString()
        val pinConfirm = pinConfirmText.text.toString()

        return if (!validatePinEnter()) {
            false
        } else if (pin != pinConfirm) {
            pinText.setBackgroundResource(R.drawable.sign_rounded_edit_texts_error)
            pinText.error = resources.getString(R.string.pinNotMatch)
            pinConfirmText.setBackgroundResource(R.drawable.sign_rounded_edit_texts_error)
            pinConfirmText.error = resources.getString(R.string.pinNotMatch)
            false
        } else {
            true
        }
    }

    private fun validatePinEnter(): Boolean {
        return if (pinText.text.length != 4) {
            pinText.setBackgroundResource(R.drawable.sign_rounded_edit_texts_error)
            pinText.error = resources.getString(R.string.pinError)
            false
        } else true
    }

    private fun invokeMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun invokeSignActivity() {
        startActivity(Intent(this, SignActivity::class.java))
        finish()
    }

    private fun invokeGeneralErrorActivity(errorText: String) {
        val snackBar = Snackbar.make(
            pinScreenLayout,
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

    private fun setThemeDefault(editText: EditText) {
        editText.setBackgroundResource(R.drawable.sign_rounded_edit_texts)
        editText.setTextColor(ContextCompat.getColor(this, R.color.colorBlue))
    }

    private fun closeKeyboard() {
        val view: View? = this.currentFocus
        view.let {
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view!!.windowToken, 0)
        }
    }
}

enum class PinScreenState {
    SETUP,
    ENTER
}
