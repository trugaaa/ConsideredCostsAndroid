package app.mobile.consideredcosts.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import app.mobile.consideredcosts.R
import app.mobile.consideredcosts.http.models.Transactions
import kotlinx.android.synthetic.main.item_empty_list.view.*
import kotlinx.android.synthetic.main.item_transactions.view.*
import java.lang.Exception

class TransactionAdapter(private var transactionList: MutableList<Transactions>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private lateinit var cont: Context


    fun updateTransactions(list: MutableList<Transactions>) {
        transactionList = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = transactionList.size + 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        cont = parent.context
        val emptyView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_list_space, parent, false)
        val emptyList =
            LayoutInflater.from(parent.context).inflate(R.layout.item_empty_list, parent, false)
        val transactions =
            LayoutInflater.from(parent.context).inflate(R.layout.item_transactions, parent, false)
        return when (viewType) {
            TYPE_EMPTY -> EmptyViewHolder(emptyView)
            TYPE_TRANSACTION -> TransactionViewHolder(transactions)
            TYPE_EMPTY_LIST -> EmptyListViewHolder(emptyList)
            else -> throw Exception()
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is TransactionViewHolder) {
            with(transactionList[position])
            {
                workType?.let {
                    holder.itemView.sourceValue.text = workType.name
                    holder.itemView.transactionMoney.text =
                        cont.getString(R.string.incomePattern, money.toString())
                    holder.itemView.sourceText.text = cont.getString(R.string.source)
                    holder.itemView.transactionMoney.setTextColor(
                        ContextCompat.getColor(
                            cont,
                            R.color.colorSuccess
                        )
                    )
                    holder.itemView.transactionCurrency.setTextColor(
                        ContextCompat.getColor(
                            cont,
                            R.color.colorSuccess
                        )
                    )
                }

                item?.let {
                    holder.itemView.sourceValue.text = item
                    holder.itemView.sourceText.text = cont.getString(R.string.item)
                    holder.itemView.transactionMoney.text =
                        cont.getString(R.string.outgoPattern, money.toString())
                    holder.itemView.transactionMoney.setTextColor(
                        ContextCompat.getColor(
                            cont,
                            R.color.colorError
                        )
                    )
                    holder.itemView.transactionCurrency.setTextColor(
                        ContextCompat.getColor(
                            cont,
                            R.color.colorError
                        )
                    )
                }

                holder.itemView.transactionDateValue.text = date
                holder.itemView.transactionTypeValue.text = type.name
                holder.itemView.transactionCurrency.text = currency

                holder.itemView.transactionDelete.setOnClickListener {
                    transactionList.removeAt(position)
                    notifyDataSetChanged()
                }
            }
        } else if (holder is EmptyListViewHolder) {
            holder.itemView.emptyListItemTitle.setText(R.string.noTransactionsAvailable)
            holder.itemView.emptyListItemText.setText(R.string.plsAddTransactions)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (transactionList.size == 0) {
            TYPE_EMPTY_LIST
        } else {
            when {
                position == transactionList.size -> TYPE_EMPTY
                position < transactionList.size -> TYPE_TRANSACTION
                else -> throw Exception()
            }
        }
    }

    companion object {
        private const val TYPE_EMPTY = 0
        private const val TYPE_TRANSACTION = 1
        private const val TYPE_EMPTY_LIST = 3
    }
}

class TransactionViewHolder(view: View) : RecyclerView.ViewHolder(view)
class EmptyListViewHolder(view: View) : RecyclerView.ViewHolder(view)
class EmptyViewHolder(view: View) : RecyclerView.ViewHolder(view)