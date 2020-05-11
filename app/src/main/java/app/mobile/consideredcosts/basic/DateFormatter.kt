package app.mobile.consideredcosts.basic

import android.content.Context
import app.mobile.consideredcosts.R
import java.text.SimpleDateFormat
import java.util.*


class DateFormatter(val context: Context) {
    private val formatter = SimpleDateFormat(FORMAT_DATE_SENT, Locale.getDefault())

    fun dateFromString(dateString: String): DateTrugaaa {
        val date: Date = formatter.parse(dateString)!!
        val calendar = Calendar.getInstance()
        calendar.time = date

        return DateTrugaaa(
            dateString,
            calendar.get(Calendar.DAY_OF_MONTH),
            calendar.get(Calendar.MONTH),
            context.getString(Months().list[calendar.get(Calendar.MONTH)]),
            calendar.get(Calendar.YEAR)
        )
    }

    fun dateGetFromCalendar(dayOfMonth: Int, monthNumber: Int, year: Int): DateTrugaaa {
        return dateFromString("$year-${monthNumber + 1}-$dayOfMonth")
    }

    companion object {
        const val FORMAT_DATE_SENT = "yyyy-MM-dd"
    }
}

data class DateTrugaaa(
    val fullFormat: String,
    val dayOfMonth: Int,
    val monthNumber: Int,
    val monthName: String,
    val year: Int
) {
    override fun toString(): String = "$dayOfMonth $monthName $year"
}

class Months {
    val list = listOf(
        R.string.month_1,
        R.string.month_2,
        R.string.month_3,
        R.string.month_4,
        R.string.month_5,
        R.string.month_6,
        R.string.month_7,
        R.string.month_8,
        R.string.month_9,
        R.string.month_10,
        R.string.month_11,
        R.string.month_12
    )
}