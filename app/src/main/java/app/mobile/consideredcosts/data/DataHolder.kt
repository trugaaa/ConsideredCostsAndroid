package app.mobile.consideredcosts.data

import app.mobile.consideredcosts.http.models.*

object DataHolder {
    var transactionsList = mutableListOf<TransactionElement>()
    var itemsList = mutableListOf<ItemElement>()
    var currencyList = mutableListOf<CurrencyElement>()
    var goalsList = mutableListOf<GoalElement>()
    var isSentToItemsAdd = false

    init {
        goalsList.add(GoalElement(1,"Active",333.33,1,"11-11-2011","11-12-2011"))
        goalsList.add(GoalElement(2,"Failed",111.33,2,"11-11-2011","11-12-2011"))
        goalsList.add(GoalElement(3,"Success",222.33,3,"11-11-2011","11-12-2011"))
    }
}