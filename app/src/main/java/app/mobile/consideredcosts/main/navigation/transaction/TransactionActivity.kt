package app.mobile.consideredcosts.main.navigation.transaction

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import app.mobile.consideredcosts.R
import app.mobile.consideredcosts.data.DataHolder
import app.mobile.consideredcosts.http.models.IncomeWorkType
import app.mobile.consideredcosts.http.models.Transactions
import app.mobile.consideredcosts.http.models.TransactionsType
import kotlinx.android.synthetic.main.activity_transaction.*
import java.lang.NullPointerException

class TransactionActivity : AppCompatActivity() {
    private val currencyList = mutableListOf<String>()
    private val typeList = mutableListOf<String>()
    private val workTypeList = mutableListOf<String>()
    private val itemTypeList = mutableListOf<String>()

    private val currencyAdapter by lazy {
        currencyList.add("EUR")
        currencyList.add("UAH")
        currencyList.add("USD")
        ArrayAdapter(this, R.layout.item_spinner, currencyList)
    }

    private val typeAdapter by lazy {
        typeList.add(TransactionsType.FAMILY.toString())
        typeList.add(TransactionsType.PRIVATE.toString())
        ArrayAdapter(this, R.layout.item_spinner, typeList)
    }

    private val workTypeAdapter by lazy {
        workTypeList.add(IncomeWorkType.SALARY.toString())
        workTypeList.add(IncomeWorkType.BUSINESS.toString())
        workTypeList.add(IncomeWorkType.TEMP_WORK.toString())
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
            when{
                transactionRadioIncome.isChecked -> {
                    DataHolder.mutableLisTransactions.add(
                        Transactions(
                            DataHolder.mutableLisTransactions.size,
                            transactionMoney.text.toString().toDouble(),
                            transactionType(transactionType),
                            transactionAddDate.text.toString(),
                            transactionCurrency.selectedItem.toString(),

                            transactionDescription.text.toString(),
                            incomeWorkType(incomeWorkType),
                            null,
                            null
                        ))
                }

                transactionRadioOutgo.isChecked -> {
                    DataHolder.mutableLisTransactions.add(
                        Transactions(
                            DataHolder.mutableLisTransactions.size,
                            transactionMoney.text.toString().toDouble(),
                            transactionType(transactionType),
                            transactionAddDate.text.toString(),
                            transactionCurrency.selectedItem.toString(),

                            transactionDescription.text.toString(),
                            null,
                            DataHolder.mutableLisTransactions.size,
                            outgoItemType.selectedItem.toString()
                        ))
                }
            }
            super.onBackPressed()
        }

        transactionOpenBack.setOnClickListener()
        {
            super.onBackPressed()
        }
    }

    private fun transactionType(type:Spinner):TransactionsType
    {
       return when(type.selectedItem.toString()){
            "FAMILY"->
            {
                 TransactionsType.FAMILY
            }
            "PRIVATE"->
            {
                 TransactionsType.PRIVATE
            }
           else -> throw NullPointerException()
       }
    }

    private fun incomeWorkType(type:Spinner):IncomeWorkType
    {
        return when(type.selectedItem.toString()){
            "SALARY"->
            {
                 IncomeWorkType.SALARY
            }
            "BUSINESS"->
            {
                 IncomeWorkType.BUSINESS
            }
            "TEMP_WORK"->
            {
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

