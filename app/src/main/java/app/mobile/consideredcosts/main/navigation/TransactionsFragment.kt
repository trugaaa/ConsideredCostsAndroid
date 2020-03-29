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
import app.mobile.consideredcosts.main.navigation.transaction.TransactionActivity
import kotlinx.android.synthetic.main.fragment_transactions.*

class TransactionsFragment : Fragment() {
    private val adapter by lazy { TransactionAdapter(mutableListOf()) }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_transactions, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        transactionAddButton.setOnClickListener(){
            startActivity(Intent(context, TransactionActivity::class.java))
        }

        transactionsViewList.layoutManager = LinearLayoutManager(context!!)
        transactionsViewList.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        adapter.updateTransactions(DataHolder.mutableLisTransactions)
    }
}
