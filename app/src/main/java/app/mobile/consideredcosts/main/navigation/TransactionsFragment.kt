package app.mobile.consideredcosts.main.navigation


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import app.mobile.consideredcosts.R
import app.mobile.consideredcosts.adapters.TransactionAdapter
import app.mobile.consideredcosts.http.models.IncomeWorkType
import app.mobile.consideredcosts.http.models.Transactions
import app.mobile.consideredcosts.http.models.TransactionsType
import kotlinx.android.synthetic.main.fragment_transactions.*

class TransactionsFragment : Fragment() {
    private val adapter by lazy { TransactionAdapter(mutableListOf()) }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_transactions, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val list = mutableListOf<Transactions>()
        for (i in 0..10) {
            list.add(
                Transactions(
                    i,
                    228.1,
                    TransactionsType.FAMILY,
                    "11/11/2001",
                    null,
                    "EUR",
                    null,
                    null,
                    "Food"
                )
            )
            list.add(
                Transactions(
                    i,
                    1000.toDouble(),
                    TransactionsType.FAMILY,
                    "11/11/2001",
                    null,
                    "USD",
                    IncomeWorkType.SALARY,
                    null,
                    null
                )
            )
        }

        transactionsViewList.layoutManager=LinearLayoutManager(context!!)
        transactionsViewList.adapter=adapter
        adapter.updateTransactions(list)
    }

}
