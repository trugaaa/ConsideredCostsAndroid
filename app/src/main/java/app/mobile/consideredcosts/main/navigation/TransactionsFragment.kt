package app.mobile.consideredcosts.main.navigation


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import app.mobile.consideredcosts.R
import app.mobile.consideredcosts.adapters.TransactionAdapter
import app.mobile.consideredcosts.data.DataHolder
import app.mobile.consideredcosts.data.SharedPreferencesManager
import app.mobile.consideredcosts.http.RetrofitClient
import app.mobile.consideredcosts.http.models.TransactionsElement
import app.mobile.consideredcosts.main.navigation.transaction.TransactionActivity
import kotlinx.android.synthetic.main.fragment_transactions.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TransactionsFragment : Fragment() {
    private val sharedPreferences by lazy {
        SharedPreferencesManager(context!!)
    }

    private val adapter by lazy {
        TransactionAdapter(mutableListOf()) { position, list ->
            deletingTransaction(list, position)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_transactions, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        transactionsViewList.layoutManager = LinearLayoutManager(context!!)
        transactionsViewList.adapter = adapter
        updateLayout(DataHolder.mutableLisTransactions)

        transactionAddButton.setOnClickListener() {
            startActivity(Intent(context, TransactionActivity::class.java))
        }
    }

    private fun updateLayout(list: MutableList<TransactionsElement>) {
        if (list.isEmpty()) {
            transactionsEmptyListLayout.visibility = View.VISIBLE
            transactionsViewList.visibility = View.GONE
        } else {
            transactionsEmptyListLayout.visibility = View.GONE
            transactionsViewList.visibility = View.VISIBLE
            adapter.updateTransactions(list)
        }
    }

    override fun onResume() {
        gettingList()
        super.onResume()

    }

    private fun deletingTransaction(list: MutableList<TransactionsElement>, position: Int) {
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                launch {
                    val response =
                        RetrofitClient.deleteTransaction(
                            "Bearer " + sharedPreferences.getToken(),
                            list[position].Id!!
                        )
                    when (response.code()) {
                        200 -> {
                            withContext(Dispatchers.Main) {
                                gettingList()
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
    }

    private fun gettingList() {
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                launch {
                    val response =
                        RetrofitClient.getTransactions("Bearer " + sharedPreferences.getToken())
                    when (response.code()) {
                        200 -> {
                            withContext(Dispatchers.Main) {
                                if (response.body()!!.data != null) {
                                    DataHolder.mutableLisTransactions =
                                        response.body()!!.data!!.list
                                } else {
                                    DataHolder.mutableLisTransactions.clear()
                                }
                                updateLayout(DataHolder.mutableLisTransactions)
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
    }
}
