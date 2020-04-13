package app.mobile.consideredcosts.http.models

import app.mobile.consideredcosts.http.BaseResponse

class  CurrenciesRequestResponse:BaseResponse<Currencies>()

data class Currencies(val list: MutableList<CurrencyElement>)

data class CurrencyElement(val Id: Int, val Name: String)