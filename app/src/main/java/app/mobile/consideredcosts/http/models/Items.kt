package app.mobile.consideredcosts.http.models

data class Items(
    val list: List<ItemElement>?
)

data class ItemElement(
    val id: Int,
    val name: String,
    val percent: Double,
    val amountOfOutgoes: Int,
    val amountOfMoney: Double,
    val currencyId:Int
)