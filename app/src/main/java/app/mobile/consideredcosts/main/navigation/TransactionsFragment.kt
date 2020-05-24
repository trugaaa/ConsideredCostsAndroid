package app.mobile.consideredcosts.main.navigation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import app.mobile.consideredcosts.R
import app.mobile.consideredcosts.adapters.TransactionAdapter
import app.mobile.consideredcosts.data.DataHolder
import app.mobile.consideredcosts.data.SharedPreferencesManager
import app.mobile.consideredcosts.http.RetrofitClient
import app.mobile.consideredcosts.http.models.TransactionElement
import app.mobile.consideredcosts.main.navigation.transaction.TransactionActivity
import app.mobile.consideredcosts.sign.PinActivity
import com.google.android.material.snackbar.Snackbar
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
        updateLayout(DataHolder.transactionsList)

        transactionAddButton.setOnClickListener {
            startActivity(Intent(context, TransactionActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        gettingList()
    }

    private fun updateLayout(list: MutableList<TransactionElement>) {
        try {
            if (list.size == 0) {
                transactionsEmptyListLayout.visibility = View.VISIBLE
                transactionsViewList.visibility = View.GONE
            } else {
                transactionsEmptyListLayout.visibility = View.GONE
                transactionsViewList.visibility = View.VISIBLE
                adapter.updateTransactions(list)
            }
        }catch (ex:java.lang.IllegalStateException)
        {
            Log.e("Crash","Trying to update transaction screen elements, when no items screen present")
        }
    }

    private fun deletingTransaction(list: MutableList<TransactionElement>, position: Int) {
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                launch {
                    val response =
                        RetrofitClient.deleteTransaction(
                            sharedPreferences.getToken()!!,
                            list[position].Id!!
                        )
                    when (response.code()) {
                        200 -> {
                            withContext(Dispatchers.Main) {
                                gettingList()
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
                                response.body()?.firstMessage()
                                    ?: resources.getString(R.string.unknownError)
                            )
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
                        RetrofitClient.getTransactions(sharedPreferences.getToken()!!)
                    when (response.code()) {
                        200 -> {
                            withContext(Dispatchers.Main) {
                                if (response.body()!!.data != null) {
                                    DataHolder.transactionsList =
                                        response.body()!!.data!!.list.filter { true } as MutableList<TransactionElement>
                                } else {
                                    DataHolder.transactionsList.clear()
                                }
                            }
                        }
                        401 -> {
                            openPinActivity()
                        }
                        504, 503, 502, 501, 500 -> {
                            invokeGeneralErrorActivity(resources.getString(R.string.serverNotAvailable))
                        }
                        else -> {
                            try {
                            invokeGeneralErrorActivity(

                                    response.body()?.firstMessage()
                                        ?: resources.getString(R.string.unknownError))
                                } catch (ex: IllegalStateException){
                                    Log.e("Crash", "Operation on TransactionFragment when screen is gone")
                                }
                        }
                    }
                }
            }
        }
        updateLayout(DataHolder.transactionsList)
    }

    private fun invokeGeneralErrorActivity(errorText: String) {
        val snackBar = Snackbar.make(
            transactionFragmentLayout,
            errorText,
            Snackbar.LENGTH_LONG
        )

        snackBar.view.setBackgroundColor(ContextCompat.getColor(context!!, R.color.colorError))
        snackBar.setActionTextColor(ContextCompat.getColor(context!!, R.color.colorPrimaryText))
        snackBar.show()
    }

    private fun openPinActivity() {
        startActivity(Intent(context, PinActivity::class.java))
        activity!!.finish()
    }
}
