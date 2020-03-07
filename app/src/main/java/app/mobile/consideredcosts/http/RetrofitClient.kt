package app.mobile.consideredcosts.http

import app.mobile.consideredcosts.http.models.request.LoginRequest
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

    suspend fun get(login: String, password: String) = api.login(LoginRequest(login, password))

}