package app.mobile.consideredcosts.http.models

import app.mobile.consideredcosts.http.BaseResponse

class UserRequestResponse : BaseResponse<User>()

data class User(
    val Id: Long,
    var FirstName: String?,
    var SecondName: String?,
    val Email:String,
    val UserName:String,
    val Money:Double,
    val CurrencyId:Long,
    val FamilyId:Long?
)