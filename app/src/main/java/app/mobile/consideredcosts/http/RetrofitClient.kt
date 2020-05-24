package app.mobile.consideredcosts.http

import app.mobile.consideredcosts.http.models.*
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
    suspend fun registration(username: String, email: String, password: String, currencyId: Int) = api.registration(
        RegistrationRequest(username, email, password,currencyId)
    )

    suspend fun getCurrencyList() = api.currencies()

    suspend fun getTransactions(token: String) = api.getTransactions(token)
    suspend fun postTransaction(token: String, transElement: TransactionElement) =
        api.postTransactions(token, transElement)

    suspend fun deleteTransaction(token: String, id: Int) = api.deleteTransaction(token, id)

    suspend fun getItems(token: String) = api.getItems(token)
    suspend fun deleteItem(token: String, id: Int) = api.deleteItem(token, id)
    suspend fun postItems(token: String, item: ItemElement) = api.postItems(token, item)

    suspend fun getGoals(token: String) = api.getGoals(token)
    suspend fun deleteGoal(token: String, id: Int) = api.deleteGoal(token, id)
    suspend fun postGoal(token: String, goal: GoalElement) = api.postGoal(token, goal)

    suspend fun getUserInfo(token: String) = api.getUserInfo(token)

    /**
     * Family
     */
    suspend fun createFamily(token: String, body: FamilyCreate) = api.createFamily(token, body)
    suspend fun getFamily(token: String) = api.getFamily(token)
    suspend fun deleteFamily(token: String) = api.deleteFamily(token)

    suspend fun inviteUser(token: String, username: String) =
        api.inviteUserToFamily(token, username)

    suspend fun leaveFamily(token: String) = api.leaveFamily(token)
    suspend fun kickUser(token: String, id: Long) = api.kickUser(token, id)

    suspend fun getInvitations(token: String) = api.getInvitations(token)
    suspend fun cancelInvitation(token: String, id: Long) = api.cancelInvitation(token, id)
    suspend fun acceptInvitation(token: String, id: Long) = api.acceptInvitation(token, id)
}