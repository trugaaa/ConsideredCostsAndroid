package app.mobile.consideredcosts.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.mobile.consideredcosts.R
import app.mobile.consideredcosts.http.models.ItemElement
import app.mobile.consideredcosts.http.models.Items
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.android.synthetic.main.item_piechart.*
import kotlinx.android.synthetic.main.item_piechart.view.*
import kotlinx.android.synthetic.main.item_product.view.*
import java.lang.Exception

class ItemsAdapter(private var itemList: MutableList<ItemElement>): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    lateinit var cont: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        cont = parent.context
        val pieView =  LayoutInflater.from(parent.context).inflate(R.layout.item_piechart,parent,false)
        val emptyView = LayoutInflater.from(parent.context).inflate(R.layout.item_product,parent,false)
        val items = LayoutInflater.from(parent.context).inflate(R.layout.item_list_space,parent,false)
        return when (viewType)
        {
            TYPE_EMPTY ->  EmptyViewHolder(emptyView)
            TYPE_PIE -> PieViewHolder(pieView)
            TYPE_ITEM -> ItemsViewHolder(items)
            else -> throw Exception()
        }
    }

    override fun getItemCount(): Int = itemList.size + 2

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when
        {
            holder is PieViewHolder->
            {
                setPieCharData(holder.itemView.itemsPieChart)
            }
            holder is ItemsViewHolder->
            {
                with(itemList[position])
                {
                    holder.itemView.percent.text = "2"
                }
            }
        }
    }

    private fun setPieCharData(itemsChart:PieChart) {
        val listPie = mutableListOf<PieEntry>()
        itemList.forEach{
            itemElement-> listPie.add(PieEntry(itemElement.percent.toFloat(),itemElement.name))
        }
        val pieDataSet = PieDataSet(listPie, "")
        pieDataSet.setColors(*ColorTemplate.COLORFUL_COLORS)
        pieDataSet.valueTextColor = Color.WHITE

        val pieData = PieData(pieDataSet)
        pieData.setValueTextSize(20F)

        itemsChart.data = pieData
        itemsChart.setEntryLabelColor(Color.WHITE)
        itemsChart.legend.isEnabled=false
        itemsChart.setUsePercentValues(true)
        itemsChart.holeRadius=20F
        itemsChart.transparentCircleRadius = 0F
        itemsChart.isDrawHoleEnabled = true
        itemsChart.description.isEnabled = false
        itemsChart.animateXY(500,500)
    }

    override fun getItemViewType(position: Int): Int {
        return when{
            position == 1 -> TYPE_PIE
            position < itemList.size  -> TYPE_ITEM
            position == itemList.size + 1 -> TYPE_EMPTY
            else -> throw Exception()
        }
    }

    companion object {
        private const val TYPE_PIE = 0
        private const val TYPE_ITEM = 1
        private const val TYPE_EMPTY = 2
    }
}

class PieViewHolder(view: View) : RecyclerView.ViewHolder(view)
class ItemsViewHolder(view: View) : RecyclerView.ViewHolder(view)
