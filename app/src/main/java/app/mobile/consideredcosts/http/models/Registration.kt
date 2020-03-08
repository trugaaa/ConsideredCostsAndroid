package app.mobile.consideredcosts.http.models

import app.mobile.consideredcosts.http.BaseResponse

class RegistrationRequestResponse: BaseResponse<LoginResponse>()

data class RegistrationResponse(val id:String,val username:String,val email: String)

data class  RegistrationRequest(val username: String,val email:String, val password:String)