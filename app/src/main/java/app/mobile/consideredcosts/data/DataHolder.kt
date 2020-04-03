package app.mobile.consideredcosts.data

import app.mobile.consideredcosts.http.models.IncomeWorkType
import app.mobile.consideredcosts.http.models.ItemElement
import app.mobile.consideredcosts.http.models.Transactions
import app.mobile.consideredcosts.http.models.TransactionsType

object DataHolder {

    var mutableLisTransactions = mutableListOf<Transactions>()
    var itemListMock = mutableListOf<ItemElement>()

    init {
        for (i in 0..10) {
            mutableLisTransactions.add(
                Transactions(
                    i,
                    228.1,
                    TransactionsType.FAMILY,
                    "11/11/2001",
                    "EUR",
                    "",
                    null,
                    null,
                    "Food"
                )
            )
            mutableLisTransactions.add(
                Transactions(
                    i,
                    1000.toDouble(),
                    TransactionsType.FAMILY,
                    "11/11/2001",
                    "EUR",
                    "dadasdasdsadasdsa",
                    IncomeWorkType.SALARY,
                    null,
                    null
                )
            )

            for (j in 0..10) {
                itemListMock.add(
                    ItemElement(j, "Food", 10.0, 10, 10000.0, 1)
                )
            }
        }
    }
}