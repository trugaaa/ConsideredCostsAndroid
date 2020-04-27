package app.mobile.consideredcosts.data

import app.mobile.consideredcosts.http.models.*

object DataHolder {

    var mutableLisTransactions = mutableListOf<TransactionsElement>()
    var itemListMock = mutableListOf<ItemElement>()
    var currencyList = mutableListOf<CurrencyElement>()
    var isSentToItemsAdd = false
}