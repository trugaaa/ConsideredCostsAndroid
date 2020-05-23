package app.mobile.consideredcosts.http.models

import app.mobile.consideredcosts.http.BaseResponse

class  CurrenciesRequestResponse:BaseResponse<Currencies>()

data class Currencies(val list: MutableList<CurrencyElement>)
{
    /**
     * Returns currency list element by Id:Int field (not list[id])
     * @param findId - id of element
     * @return CurrencyElement
     */
    fun findElementByListId(findId: Int):CurrencyElement?
    {
       return list.find { it.Id == findId }
    }
}

data class CurrencyElement(val Id: Int, val Name: String)