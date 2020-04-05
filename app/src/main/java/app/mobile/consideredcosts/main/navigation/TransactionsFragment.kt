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
import app.mobile.consideredcosts.http.models.Transactions
import app.mobile.consideredcosts.main.navigation.transaction.TransactionActivity
import kotlinx.android.synthetic.main.fragment_transactions.*

class TransactionsFragment : Fragment() {
    private val adapter by lazy {
        TransactionAdapter(mutableListOf()) { position, list ->
            list.removeAt(position)
            updateLayout(list)
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

    private fun updateLayout(list: MutableList<Transactions>) {
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
        super.onResume()
        updateLayout(DataHolder.mutableLisTransactions)
    }
}
