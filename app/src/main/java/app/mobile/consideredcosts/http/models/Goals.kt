package app.mobile.consideredcosts.http.models

import app.mobile.consideredcosts.http.BaseResponse

class GoalsRequestResponse: BaseResponse<Goals>()

data class GoalElement(var Id:Int?,val Status:String?,val Money:Double,val CurrencyId:Int, val DateStart:String, val DateFinish:String)

data class Goals(val list: MutableList<GoalElement>?)