package app.mobile.consideredcosts.http

import app.mobile.consideredcosts.http.models.LoginRequest
import app.mobile.consideredcosts.http.models.LoginRequestResponse
import app.mobile.consideredcosts.http.models.RegistrationRequest
import app.mobile.consideredcosts.http.models.RegistrationRequestResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface Api {
    @POST("accounts/login")
    suspend fun login(@Body loginParameters: LoginRequest): Response<LoginRequestResponse>

    @POST("accounts/registration")
    suspend fun registration(@Body registrationParameters: RegistrationRequest): Response<RegistrationRequestResponse>
}