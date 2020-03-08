package app.mobile.consideredcosts.http

import app.mobile.consideredcosts.http.models.LoginRequest
import app.mobile.consideredcosts.http.models.RegistrationRequest
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

}