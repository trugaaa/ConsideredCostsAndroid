package app.mobile.consideredcosts.http

import app.mobile.consideredcosts.http.models.request.LoginRequest
import app.mobile.consideredcosts.http.models.response.LoginRequestResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface Api {
@POST("accounts/login")
suspend fun login(@Body loginParameters : LoginRequest): Response <LoginRequestResponse>

}