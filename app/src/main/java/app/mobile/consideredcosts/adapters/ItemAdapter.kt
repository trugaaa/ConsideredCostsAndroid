package app.mobile.consideredcosts.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.mobile.consideredcosts.R
import app.mobile.consideredcosts.data.DataHolder
import app.mobile.consideredcosts.http.models.ItemElement
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.android.synthetic.main.item_piechart.view.*
import kotlinx.android.synthetic.main.item_product.view.*

class ItemsAdapter(
    private var itemList: MutableList<ItemElement>,
    val click: (Int, MutableList<ItemElement>) -> Unit
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    lateinit var cont: Context
    var showPie: Boolean = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        cont = parent.context
        val pieView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_piechart, parent, false)
        val emptyView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_list_space, parent, false)
        val items =
            LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)

        return when (viewType) {
            TYPE_EMPTY -> EmptyViewHolder(emptyView)
            TYPE_PIE -> PieViewHolder(pieView)
            TYPE_ITEM -> ItemsViewHolder(items)
            else -> throw Exception()
        }
    }

    override fun getItemCount(): Int = itemList.size + 2

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is PieViewHolder -> {
                if (showPie) {
                    setPieCharData(holder.itemView.itemsPieChart)
                } else {
                    holder.itemView.itemsPieChart.visibility = View.GONE
                }
            }
            is ItemsViewHolder -> {
                with(itemList[position - 1])
                {
                    holder.itemView.textProductValue.text = Name
                    holder.itemView.amountOfMoneyValue.text = AmountOfMoney.toString()
                    holder.itemView.amountOfOutgoesValue.text = AmountOfOutgoes.toString()
                    holder.itemView.percentValue.text =
                        cont.getString(R.string.percentPattern, Percent.toString(), " %")
                    holder.itemView.amountOfMoneyCurrencyValue.text =
                        DataHolder.currencyList.find { currencyElement ->
                            currencyElement.Id == CurrencyId
                        }!!.Name
                }

                holder.itemView.itemDelete.setOnClickListener {
                    click(position - 1, itemList)
                }
            }
        }
    }

    private fun setPieCharData(itemsChart: PieChart) {
        val listPie = mutableListOf<PieEntry>()
        itemList.forEach { itemElement ->
            listPie.add(PieEntry(itemElement.Percent!!.toFloat(), itemElement.Name))
        }
        val pieDataSet = PieDataSet(listPie, "")
        pieDataSet.setColors(*ColorTemplate.COLORFUL_COLORS)
        pieDataSet.valueTextColor = Color.WHITE
        val pieData = PieData(pieDataSet)
        pieData.setValueTextSize(20F)
        itemsChart.data = pieData
        itemsChart.setEntryLabelColor(Color.WHITE)
        itemsChart.legend.isEnabled = false
        itemsChart.setUsePercentValues(true)
        itemsChart.holeRadius = 20F
        itemsChart.transparentCircleRadius = 0F
        itemsChart.isDrawHoleEnabled = true
        itemsChart.description.isEnabled = false
        itemsChart.animateXY(500, 500)
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            position == 0 -> TYPE_PIE
            position < itemList.size + 1 -> TYPE_ITEM
            position == itemList.size + 1 -> TYPE_EMPTY
            else -> throw Exception()
        }
    }

    private fun elementsNotEmpty(list: MutableList<ItemElement>): Boolean {
        list.forEach {
            if (it.Percent != 0.0) return true
        }
        return false
    }

    fun listUpdate(list: MutableList<ItemElement>) {
        showPie = elementsNotEmpty(list)
        itemList = list
        notifyDataSetChanged()
    }

    companion object {
        private const val TYPE_PIE = 0
        private const val TYPE_ITEM = 1
        private const val TYPE_EMPTY = 2
    }
}

class PieViewHolder(view: View) : RecyclerView.ViewHolder(view)
class ItemsViewHolder(view: View) : RecyclerView.ViewHolder(view)
