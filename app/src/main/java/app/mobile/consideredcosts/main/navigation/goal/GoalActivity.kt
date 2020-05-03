package app.mobile.consideredcosts.main.navigation.goal

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import app.mobile.consideredcosts.R
import app.mobile.consideredcosts.basic.DateFormatter
import app.mobile.consideredcosts.basic.DateTrugaaa
import app.mobile.consideredcosts.data.DataHolder
import app.mobile.consideredcosts.data.SharedPreferencesManager
import app.mobile.consideredcosts.http.RetrofitClient
import app.mobile.consideredcosts.http.models.GoalElement
import app.mobile.consideredcosts.sign.PinActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_goal.*
import kotlinx.android.synthetic.main.activity_goal.goalCurrency
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class GoalActivity : AppCompatActivity() {
    private lateinit var goalStartDateForSend: DateTrugaaa
    private lateinit var goalEndDateForSend: DateTrugaaa

    private val currencyAdapter by lazy {
        ArrayAdapter(this, R.layout.item_spinner, DataHolder.currencyList.map {
            it.Name
        })
    }
    private val sharedPreferences by lazy {
        SharedPreferencesManager(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goal)
        this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

        updateComboFields()
        goalMoney.setOnTouchListener { _, _ ->
            setThemeDefault(goalMoney)
            false
        }

        goalMoney.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                validateMoney()
            }
        }


        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        goalEndDate.setOnClickListener {
            try {
                closeKeyboard()
            } catch (ex: Exception) {
                Log.w("WARNING", ex.stackTrace.toString())
            }

            setThemeDefault(goalEndDate)
            goalEndDate.error = null

            val datePickerDialog = DatePickerDialog(
                this, R.style.DataPickerStyle,
                DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    goalEndDateForSend =
                        DateFormatter(this).dateGetFromCalendar(dayOfMonth, month, year)
                    goalEndDate.text = goalEndDateForSend.toString()
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }

        goalStartDate.setOnClickListener {
            try {
                closeKeyboard()
            } catch (ex: Exception) {
                Log.w("WARNING", ex.stackTrace.toString())
            }
            setThemeDefault(goalStartDate)
            goalStartDate.error = null

            val datePickerDialog = DatePickerDialog(
                this, R.style.DataPickerStyle,
                DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    goalStartDateForSend =
                        DateFormatter(this).dateGetFromCalendar(dayOfMonth, month, year)
                    goalStartDate.text = goalStartDateForSend.toString()
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }

        goalAddButton.setOnClickListener {
            if (validateMoney() and validateEndDate() and validateStartDate()) {
                goalSaving(
                    GoalElement(
                        null,
                        null,
                        goalMoney.text.toString().toDouble(),
                        DataHolder.currencyList.find { currencyElement ->
                            currencyElement.Name == goalCurrency.selectedItem.toString()
                        }!!.Id,
                        goalStartDateForSend.fullFormat,
                        goalEndDateForSend.fullFormat
                    )
                )
                super.onBackPressed()
            }
        }

        goalOpenBack.setOnClickListener()
        {
            super.onBackPressed()
        }
    }

    private fun setThemeDefault(editText: TextView) {
        editText.setBackgroundResource(R.drawable.transaction_combo_background)
        editText.setTextColor(ContextCompat.getColor(this, R.color.colorBlue))
    }

    private fun validateMoney(): Boolean {
        return if (goalMoney.text.toString() == "") {
            goalMoney.error = resources.getString(R.string.errorFieldIsRequired)
            goalMoney.setBackgroundResource(R.drawable.transaction_combo_background_error)
            false
        } else true
    }

    private fun validateStartDate(): Boolean {
        return if (goalStartDate.text.toString() == "") {
            goalStartDate.error = resources.getString(R.string.errorFieldIsRequired)
            goalStartDate.setBackgroundResource(R.drawable.transaction_combo_background_error)
            false
        } else true
    }

    private fun validateEndDate(): Boolean {
        return if (goalEndDate.text.toString() == "") {
            goalEndDate.error = resources.getString(R.string.errorFieldIsRequired)
            goalEndDate.setBackgroundResource(R.drawable.transaction_combo_background_error)
            false
        } else true
    }

    private fun goalSaving(goal: GoalElement) {
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                launch {
                    val response = RetrofitClient.postGoal(
                        sharedPreferences.getToken()!!,
                        goal
                    )
                    when (response.code()) {
                        200 -> {
                            withContext(Dispatchers.Main) {
                                super.onBackPressed()
                            }
                        }
                        401 -> {
                            openPinActivity()
                        }
                        504, 503, 502, 501, 500 -> {
                            invokeGeneralErrorActivity(resources.getString(R.string.serverNotAvailable))
                        }
                        else -> {
                            invokeGeneralErrorActivity(
                                response.body()?.firstMessage
                                    ?: resources.getString(R.string.unknownError)
                            )
                        }
                    }
                }
            }
        }
    }

    private fun invokeGeneralErrorActivity(errorText: String) {
        val snackBar = Snackbar.make(
            activityGoalLayout,
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

    private fun closeKeyboard() {
        val view: View? = this.currentFocus
        view.let {
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view!!.windowToken, 0)
        }
    }

    private fun updateComboFields() {
        goalCurrency.adapter = currencyAdapter
    }

    private fun openPinActivity() {
        startActivity(Intent(this, PinActivity::class.java))
        finish()
    }
}
