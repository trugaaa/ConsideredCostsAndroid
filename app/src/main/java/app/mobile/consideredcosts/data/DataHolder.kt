package app.mobile.consideredcosts.data

import app.mobile.consideredcosts.http.models.*

object DataHolder {
    var transactionsList = mutableListOf<TransactionElement>()
    var itemsList = mutableListOf<ItemElement>()
    var currencyList = mutableListOf<CurrencyElement>()
    var goalsList = mutableListOf<GoalElement>()
    var isSentToItemsAdd = false
    var family: Family? = null
    var hasFamily = false
    var userInfo: User? = null
}