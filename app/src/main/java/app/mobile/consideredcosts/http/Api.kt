package app.mobile.consideredcosts.http

import app.mobile.consideredcosts.http.models.*
import retrofit2.Response
import retrofit2.http.*

interface Api {
    @POST("accounts/login")
    suspend fun login(@Body loginParameters: LoginRequest):
            Response<LoginRequestResponse>

    @POST("accounts/registration")
    suspend fun registration(@Body registrationParameters: RegistrationRequest):
            Response<RegistrationRequestResponse>

    @GET("currencies")
    suspend fun currencies():
            Response<CurrenciesRequestResponse>

    @GET("transactions")
    suspend fun getTransactions(@Header("Authorization") token: String):
            Response<TransactionsRequestResponse>

    @DELETE("transactions")
    suspend fun deleteTransaction(@Header("Authorization") token: String, @Header("Id") id: Int):
            Response<TransactionsRequestResponse>

    @POST("transactions")
    suspend fun postTransactions(@Header("Authorization") token: String, @Body transactionParameters: TransactionsElement):
            Response<TransactionsRequestResponse>

    @GET("items")
    suspend fun getItems(@Header("Authorization") token: String):
            Response<ItemsRequestResponse>

    @POST("items")
    suspend fun postItems(@Header("Authorization") token: String, @Body item:ItemElement):
            Response<ItemsRequestResponse>

    @DELETE("items")
    suspend fun deleteItem(@Header("Authorization") token: String, @Header("Id") id: Int):
            Response<ItemsRequestResponse>
}