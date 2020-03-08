package app.mobile.consideredcosts.http.models

import app.mobile.consideredcosts.http.BaseResponse

class LoginRequestResponse:BaseResponse<LoginResponse>()

data class LoginResponse(val access_token:String,val username:String)

data class LoginRequest(val username: String,val password:String)