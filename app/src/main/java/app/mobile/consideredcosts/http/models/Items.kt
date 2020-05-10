package app.mobile.consideredcosts.http.models

import app.mobile.consideredcosts.http.BaseResponse

class ItemsRequestResponse:BaseResponse<Items>()

data class Items(
    val list: MutableList<ItemElement>?
)

data class ItemElement(
    val Id: Int?,
    val Name: String,
    val Percent: Double?,
    val AmountOfOutgoes: Int?,
    val AmountOfMoney: Double?,
    val CurrencyId: Int?
)