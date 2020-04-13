package app.mobile.consideredcosts.main.navigation.transaction

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import app.mobile.consideredcosts.R
import app.mobile.consideredcosts.data.DataHolder
import app.mobile.consideredcosts.data.DataHolder.currencyList
import app.mobile.consideredcosts.data.SharedPreferencesManager
import app.mobile.consideredcosts.http.RetrofitClient
import app.mobile.consideredcosts.http.models.IncomeWorkType
import app.mobile.consideredcosts.http.models.TransactionsElement
import app.mobile.consideredcosts.http.models.TransactionsType
import kotlinx.android.synthetic.main.activity_transaction.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.NullPointerException

class TransactionActivity : AppCompatActivity() {
    private val typeList = mutableListOf<String>()
    private val workTypeList = mutableListOf<String>()
    private val itemTypeList = mutableListOf<String>()
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
        itemTypeList.add("Food")
        itemTypeList.add("Transport")
        itemTypeList.add("Audi TT")
        ArrayAdapter(this, R.layout.item_spinner, itemTypeList)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction)
        updateComboFields()

        transactionRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.transactionRadioIncome -> {
                    incomeWorkType.visibility = View.VISIBLE
                    outgoItemType.visibility = View.GONE
                }
                R.id.transactionRadioOutgo -> {
                    incomeWorkType.visibility = View.GONE
                    outgoItemType.visibility = View.VISIBLE
                }
            }
        }

        transactionAddButton.setOnClickListener {
            val transToSend: TransactionsElement = when {
                transactionRadioIncome.isChecked -> {
                    TransactionsElement(
                        null,
                        transactionMoney.text.toString().toDouble(),
                        transactionType(transactionType),
                        transactionAddDate.text.toString(),
                        currencyList.find { currencyElement -> currencyElement.Name == transactionCurrency.selectedItem.toString() }!!.Id,

                        transactionDescription.text.toString(),
                        incomeWorkType(incomeWorkType),
                        null
                    )
                }

                transactionRadioOutgo.isChecked -> {
                    TransactionsElement(
                        null,
                        transactionMoney.text.toString().toDouble(),
                        transactionType(transactionType),
                        transactionAddDate.text.toString(),
                        currencyList.find { currencyElement -> currencyElement.Name == transactionCurrency.selectedItem.toString() }!!.Id,

                        transactionDescription.text.toString(),
                        null,
                        1
                    )
                }
                else -> throw Exception()
            }

            GlobalScope.launch {
                withContext(Dispatchers.IO) {
                    launch {
                        val response = RetrofitClient.postTransaction(
                            "Bearer " + sharedPreferences.getToken()!!,
                            transToSend
                        )
                        when (response.code()) {
                            200 -> {
                                withContext(Dispatchers.Main){
                                    super.onBackPressed()
                                }
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




            super.onBackPressed()
        }

        transactionOpenBack.setOnClickListener()
        {
            super.onBackPressed()
        }
    }

    private fun transactionType(type: Spinner): TransactionsType {
        return when (type.selectedItem.toString()) {
            "Family" -> {
                TransactionsType.FAMILY
            }
            "Private" -> {
                TransactionsType.PRIVATE
            }
            else -> throw NullPointerException()
        }
    }

    private fun incomeWorkType(type: Spinner): IncomeWorkType {
        return when (type.selectedItem.toString()) {
            "Salary" -> {
                IncomeWorkType.SALARY
            }
            "Business" -> {
                IncomeWorkType.BUSINESS
            }
            "Temporary work" -> {
                IncomeWorkType.TEMP_WORK
            }
            else -> throw NullPointerException()
        }

    }

    private fun updateComboFields() {
        transactionCurrency.adapter = currencyAdapter
        transactionType.adapter = typeAdapter

        incomeWorkType.adapter = workTypeAdapter
        outgoItemType.adapter = itemTypeAdapter
    }
}

