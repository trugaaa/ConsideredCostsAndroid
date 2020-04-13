package app.mobile.consideredcosts.http

import app.mobile.consideredcosts.http.models.LoginRequest
import app.mobile.consideredcosts.http.models.RegistrationRequest
import app.mobile.consideredcosts.http.models.TransactionsElement
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL = "https://ccostsproject.azurewebsites.net/api/"
    private val api: Api

    init {
        api = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).client(
                OkHttpClient.Builder().addInterceptor(
                    HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    }).build()
            )
            .build()
            .create(Api::class.java)
    }

    suspend fun login(login: String, password: String) = api.login(LoginRequest(login, password))
    suspend fun registration(username: String,email:String, password: String) = api.registration(
        RegistrationRequest(username, email, password))
    suspend fun getCurrencyList() = api.currencies()
    suspend fun getTransactions(token:String) = api.getTransactions(token)
    suspend fun postTransaction(token: String, transElement: TransactionsElement) = api.postTransactions(token,transElement)
    suspend fun deleteTransaction(token: String, id:Int) = api.deleteTransaction(token,id)
    suspend fun getItems(token: String) = api.getItems(token)
    suspend fun deleteItem(token: String, id:Int) = api.deleteItem(token,id)
}