package app.mobile.consideredcosts.http.models
import app.mobile.consideredcosts.http.BaseResponse

class  TransactionsRequestResponse: BaseResponse<Transactions>()
data class Transactions(val list: MutableList<TransactionElement>)
data class TransactionElement(
    val Id: Int?,
    val Money: Double,
    val Type: TransactionsType,
    val Date: String,
    val CurrencyId: Int,

    val Description: String?,
    val WorkType: IncomeWorkType?,
    val ItemId:Int?
)


enum class IncomeWorkType{
    Salary, Business, TempWork
}

enum class TransactionsType {
    Family, Private
}