package app.mobile.consideredcosts.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import app.mobile.consideredcosts.R
import app.mobile.consideredcosts.data.DataHolder
import app.mobile.consideredcosts.http.models.GoalElement
import kotlinx.android.synthetic.main.item_goals.view.*
import java.lang.Exception

class GoalAdapter(
    private var goalsList: MutableList<GoalElement>,
    val click: (MutableList<GoalElement>, Int) -> Unit
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        val emptyView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_list_space, parent, false)
        val goals =
            LayoutInflater.from(parent.context).inflate(R.layout.item_goals, parent, false)
        return when (viewType) {
            TYPE_EMPTY -> EmptyViewHolder(emptyView)
            TYPE_GOALS -> GoalViewHolder(goals)
            else -> throw Exception()
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is GoalViewHolder) {
            with(goalsList[position])
            {
                holder.itemView.goalValue.text = Money.toString()
                holder.itemView.goalCurrency.text = DataHolder.currencyList[CurrencyId].Name
                holder.itemView.startDateValue.text = DateStart
                holder.itemView.endDateValue.text = DateFinish
                holder.itemView.statusValue.text = Status
                when (Status) {
                    "Success" -> holder.itemView.statusValue.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.colorSuccess
                        )
                    )
                    "Active" -> holder.itemView.statusValue.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.colorProgress
                        )
                    )
                    "Failed" -> holder.itemView.statusValue.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.colorError
                        )
                    )
                }
            }

            holder.itemView.goalDelete.setOnClickListener{
                click(goalsList,position)
                notifyDataSetChanged()
            }
        }
    }

    override fun getItemCount(): Int = goalsList.size + 1

    fun updateGoals(list: MutableList<GoalElement>) {
        goalsList = list
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            position == goalsList.size -> TYPE_EMPTY
            position < goalsList.size -> TYPE_GOALS
            else -> throw Exception()
        }
    }

    companion object {
        private const val TYPE_EMPTY = 0
        private const val TYPE_GOALS = 1
    }
}

class GoalViewHolder(view: View) : RecyclerView.ViewHolder(view)

