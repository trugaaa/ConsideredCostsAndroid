package app.mobile.consideredcosts.main.navigation.transaction

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import app.mobile.consideredcosts.R
import app.mobile.consideredcosts.basic.DateFormatter
import app.mobile.consideredcosts.basic.DateTrugaaa
import app.mobile.consideredcosts.data.DataHolder.currencyList
import app.mobile.consideredcosts.data.DataHolder.isSentToItemsAdd
import app.mobile.consideredcosts.data.DataHolder.itemsList
import app.mobile.consideredcosts.data.SharedPreferencesManager
import app.mobile.consideredcosts.http.RetrofitClient
import app.mobile.consideredcosts.http.models.IncomeWorkType
import app.mobile.consideredcosts.http.models.TransactionElement
import app.mobile.consideredcosts.http.models.TransactionsType
import app.mobile.consideredcosts.sign.PinActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_transaction.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*


class TransactionActivity : AppCompatActivity() {
    private lateinit var transactionDateForSend: DateTrugaaa
    private lateinit var calendar: Calendar

    private val typeList = mutableListOf<String>()
    private val workTypeList = mutableListOf<String>()
    private val sharedPreferences by lazy {
        SharedPreferencesManager(this)
    }
    private val currencyAdapter by lazy {
        ArrayAdapter(this, R.layout.item_spinner, currencyList.map {
            it.Name
        })
    }
    private val typeAdapter by lazy {
        typeList.add("Family")
        typeList.add("Private")
        ArrayAdapter(this, R.layout.item_spinner, typeList)
    }

    private val workTypeAdapter by lazy {
        workTypeList.add("Salary")
        workTypeList.add("Business")
        workTypeList.add("Temporary work")
        ArrayAdapter(this, R.layout.item_spinner, workTypeList)
    }
    private val itemTypeAdapter by lazy {
        ArrayAdapter(this, R.layout.item_spinner, itemsList.map {
            it.Name
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction)
        this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

        updateComboFields()
        calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        transactionMoney.setOnTouchListener { _, _ ->
            setThemeDefault(transactionMoney)
            false
        }

        transactionMoney.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                validateMoney()
            }
        }

        transactionAddDate.setOnClickListener {
            try {
                closeKeyboard()
            } catch (ex: Exception) {
                Log.w("WARNING", ex.stackTrace.toString())
            }
            setThemeDefault(transactionAddDate)
            transactionAddDate.error = null

            val datePickerDialog = DatePickerDialog(
                this, R.style.DataPickerStyle,
                DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    transactionDateForSend =
                        DateFormatter(this).dateGetFromCalendar(dayOfMonth, month, year)
                    transactionAddDate.text = transactionDateForSend.toString()

                },
                year,
                month,
                day
            )

            datePickerDialog.show()
        }

        transactionRadioGroup.setOnCheckedChangeListener { _, checkedId ->

            when (checkedId) {
                R.id.transactionRadioIncome -> {
                    layoutTransaction.visibility = View.VISIBLE
                    itemsEmptyOnOutgoAdd.visibility = View.GONE
                    incomeWorkType.visibility = View.VISIBLE
                    outgoItemType.visibility = View.GONE
                    transactionAddButton.text = resources.getString(R.string.add)
                }
                R.id.transactionRadioOutgo -> {
                    if (itemsList.isNullOrEmpty()) {
                        transactionAddButton.text = resources.getString(R.string.addItems)
                        layoutTransaction.visibility = View.GONE
                        itemsEmptyOnOutgoAdd.visibility = View.VISIBLE
                    } else {
                        transactionAddButton.text = resources.getString(R.string.add)
                        layoutTransaction.visibility = View.VISIBLE
                        itemsEmptyOnOutgoAdd.visibility = View.GONE
                        incomeWorkType.visibility = View.GONE
                        outgoItemType.visibility = View.VISIBLE
                    }
                }
            }
        }

        transactionAddButton.setOnClickListener {
            if (itemsList.isNullOrEmpty()) {
                isSentToItemsAdd = true
            }

            if (validateDate() and validateMoney()) {
                val transToSend: TransactionElement? = when {
                    transactionRadioIncome.isChecked -> {
                        isSentToItemsAdd = false
                        TransactionElement(
                            null,
                            transactionMoney.text.toString().toDouble(),
                            transactionType(transactionType),
                            transactionDateForSend.fullFormat,
                            currencyList.find { currencyElement -> currencyElement.Name == transactionCurrency.selectedItem.toString() }!!.Id,
                            transactionDescription.text.toString(),
                            incomeWorkType(incomeWorkType),
                            null
                        )
                    }
                    transactionRadioOutgo.isChecked -> {
                        if (!isSentToItemsAdd) {
                            TransactionElement(
                                null,
                                transactionMoney.text.toString().toDouble(),
                                transactionType(transactionType),
                                transactionDateForSend.fullFormat,
                                currencyList.find { currencyElement ->
                                    currencyElement.Name ==
                                            transactionCurrency.selectedItem.toString()
                                }!!.Id,
                                transactionDescription.text.toString(),
                                null,
                                itemsList.find { itemElement ->
                                    itemElement.Name ==
                                            outgoItemType.selectedItem.toString()
                                }!!.Id
                            )
                        } else {
                            null
                        }
                    }
                    else -> throw Exception()
                }

                if (!isSentToItemsAdd) {
                    GlobalScope.launch {
                        withContext(Dispatchers.IO) {
                            launch {
                                val response = RetrofitClient.postTransaction(
                                    sharedPreferences.getToken()!!,
                                    transToSend!!
                                )
                                when (response.code()) {
                                    200 -> {
                                        try {
                                            withContext(Dispatchers.Main) {
                                                super.onBackPressed()
                                            }
                                        } catch (ex: Exception) {
                                            invokeGeneralErrorActivity(
                                                response.body()?.firstMessage
                                                    ?: resources.getString(R.string.unknownError)
                                            )
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
            }

            if (isSentToItemsAdd) {
                when {
                    transactionRadioIncome.isChecked -> {
                    }
                    transactionRadioOutgo.isChecked -> {
                        super.onBackPressed()
                    }
                }
            }
        }

        transactionOpenBack.setOnClickListener()
        {
            super.onBackPressed()
        }
    }

    private fun transactionType(type: Spinner): TransactionsType {
        return when (type.selectedItem.toString()) {
            "Family" -> {
                TransactionsType.Family
            }
            "Private" -> {
                TransactionsType.Private
            }
            else -> throw NullPointerException()
        }
    }

    private fun incomeWorkType(type: Spinner): IncomeWorkType {
        return when (type.selectedItem.toString()) {
            "Salary" -> {
                IncomeWorkType.Salary
            }
            "Business" -> {
                IncomeWorkType.Business
            }
            "Temporary work" -> {
                IncomeWorkType.TempWork
            }
            else -> throw KotlinNullPointerException()
        }

    }

    private fun invokeGeneralErrorActivity(errorText: String) {
        val snackBar = Snackbar.make(
            transactionActivityLayout,
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

    private fun setThemeDefault(editText: TextView) {
        editText.setBackgroundResource(R.drawable.transaction_combo_background)
        editText.setTextColor(ContextCompat.getColor(this, R.color.colorBlue))
    }

    private fun updateComboFields() {
        transactionCurrency.adapter = currencyAdapter
        transactionType.adapter = typeAdapter

        incomeWorkType.adapter = workTypeAdapter
        outgoItemType.adapter = itemTypeAdapter
    }


    private fun validateMoney(): Boolean {
        return if (transactionMoney.text.toString() == "") {
            transactionMoney.error = resources.getString(R.string.errorFieldIsRequired)
            transactionMoney.setBackgroundResource(R.drawable.transaction_combo_background_error)

            false
        } else true
    }

    private fun validateDate(): Boolean {
        return if (transactionAddDate.text.toString() == "") {
            transactionAddDate.error = resources.getString(R.string.errorFieldIsRequired)
            transactionAddDate.setBackgroundResource(R.drawable.transaction_combo_background_error)
            false
        } else true
    }

    private fun closeKeyboard() {
        val view: View? = this.currentFocus
        view.let {
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view!!.windowToken, 0)
        }
    }

    private fun openPinActivity() {
        startActivity(Intent(this, PinActivity::class.java))
        finish()
    }
}

