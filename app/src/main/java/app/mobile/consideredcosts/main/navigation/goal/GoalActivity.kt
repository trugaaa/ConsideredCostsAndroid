package app.mobile.consideredcosts.main.navigation.goal

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.ArrayAdapter
import app.mobile.consideredcosts.R
import app.mobile.consideredcosts.data.DataHolder
import app.mobile.consideredcosts.http.models.GoalElement
import kotlinx.android.synthetic.main.activity_goal.*
import kotlinx.android.synthetic.main.activity_goal.goalCurrency
import java.util.*

class GoalActivity : AppCompatActivity() {
    private val currencyAdapter by lazy {
        ArrayAdapter(this, R.layout.item_spinner, DataHolder.currencyList.map {
            it.Name
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goal)
        this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        updateComboFields()

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        goalEndDate.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                this, R.style.DataPickerStyle,
                DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    goalEndDate.text = applicationContext.getString(
                        R.string.datePattern,
                        dayOfMonth.toString(),
                        month.toString(),
                        year.toString()
                    )
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }

        goalStartDate.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                this, R.style.DataPickerStyle,
                DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    goalStartDate.text = applicationContext.getString(
                        R.string.datePattern,
                        dayOfMonth.toString(),
                        month.toString(),
                        year.toString()
                    )
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }

        goalAddButton.setOnClickListener {
            DataHolder.goalsList.add(
                GoalElement(
                    DataHolder.goalsList.size,
                    "Active",
                    goalMoney.text.toString().toDouble(),
                    DataHolder.currencyList.find { currencyElement ->
                        currencyElement.Name == goalCurrency.selectedItem.toString()
                    }!!.Id,
                    goalStartDate.text.toString(),
                    goalEndDate.text.toString()
                )
            )
            super.onBackPressed()
        }

        goalOpenBack.setOnClickListener()
        {
            super.onBackPressed()
        }
    }

    private fun updateComboFields() {
        goalCurrency.adapter = currencyAdapter
    }
}