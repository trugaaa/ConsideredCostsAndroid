package app.mobile.consideredcosts.http.models

data class Transactions(
    val id: Int,
    val money: Double,
    val type: TransactionsType,
    val date: String,
    val description: String?,
    val currency: String,
    val workType: IncomeWorkType?,
    val itemId:Int?,
    val item:String?
)


enum class IncomeWorkType {
    SALARY, BUSINESS, TEMP_WORK
}

enum class TransactionsType {
    FAMILY, PRIVATE
}