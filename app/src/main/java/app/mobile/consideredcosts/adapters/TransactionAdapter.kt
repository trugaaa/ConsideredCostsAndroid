package app.mobile.consideredcosts.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import app.mobile.consideredcosts.R
import app.mobile.consideredcosts.data.DataHolder
import app.mobile.consideredcosts.http.models.TransactionElement
import kotlinx.android.synthetic.main.item_transactions.view.*
import java.lang.Exception

class TransactionAdapter(private var transactionList: MutableList<TransactionElement>, val click: (Int, MutableList<TransactionElement>) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private lateinit var cont: Context


    fun updateTransactions(list: MutableList<TransactionElement>) {
        transactionList = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = transactionList.size + 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        cont = parent.context
        val emptyView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_list_space, parent, false)
        val transactions =
            LayoutInflater.from(parent.context).inflate(R.layout.item_transactions, parent, false)
        return when (viewType) {
            TYPE_EMPTY -> EmptyViewHolder(emptyView)
            TYPE_TRANSACTION -> TransactionViewHolder(transactions)
            else -> throw Exception()
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is TransactionViewHolder) {
            with(transactionList[position])
            {
                WorkType?.let {
                    holder.itemView.sourceValue.text = WorkType.name.toLowerCase().capitalize()
                    holder.itemView.transactionMoney.text =
                        cont.getString(R.string.incomePattern, Money.toString())
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

                ItemId?.let {
               //     holder.itemView.sourceValue.text = item
                    holder.itemView.sourceText.text = cont.getString(R.string.item)
                    holder.itemView.transactionMoney.text =
                        cont.getString(R.string.outgoPattern, Money.toString())
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

                holder.itemView.transactionDateValue.text = Date
                holder.itemView.transactionTypeValue.text = Type.name.toLowerCase().capitalize()

                holder.itemView.transactionCurrency.text = DataHolder.currencyList.find{
                    currencyElement -> currencyElement.Id == CurrencyId
                }!!.Name

                holder.itemView.transactionDelete.setOnClickListener {
                    click(position,transactionList)
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
            return when {
                position == transactionList.size -> TYPE_EMPTY
                position < transactionList.size -> TYPE_TRANSACTION
                else -> throw Exception()
            }
    }

    companion object {
        private const val TYPE_EMPTY = 0
        private const val TYPE_TRANSACTION = 1
    }
}

class TransactionViewHolder(view: View) : RecyclerView.ViewHolder(view)
class EmptyViewHolder(view: View) : RecyclerView.ViewHolder(view)