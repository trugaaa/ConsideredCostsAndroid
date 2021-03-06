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
    suspend fun postTransactions(
        @Header("Authorization") token: String,
        @Body transactionParameters: TransactionElement
    ):
            Response<TransactionsRequestResponse>

    @GET("items")
    suspend fun getItems(@Header("Authorization") token: String):
            Response<ItemsRequestResponse>

    @POST("items")
    suspend fun postItems(@Header("Authorization") token: String, @Body item: ItemElement):
            Response<ItemsRequestResponse>

    @DELETE("items")
    suspend fun deleteItem(@Header("Authorization") token: String, @Header("Id") id: Int):
            Response<ItemsRequestResponse>

    @GET("goals")
    suspend fun getGoals(@Header("Authorization") token: String):
            Response<GoalsRequestResponse>

    @POST("goals")
    suspend fun postGoal(@Header("Authorization") token: String, @Body goal: GoalElement):
            Response<GoalsRequestResponse>

    @DELETE("goals")
    suspend fun deleteGoal(@Header("Authorization") token: String, @Header("Id") id: Int):
            Response<GoalsRequestResponse>

    @GET("accounts")
    suspend fun getUserInfo(@Header("Authorization") token: String):
            Response<UserRequestResponse>

    /**
     * Family Requests
     */
    @GET("family")
    suspend fun getFamily(@Header("Authorization") token: String):
            Response<FamilyBaseResponse>

    @POST("family")
    suspend fun createFamily(@Header("Authorization") token: String, @Body family: FamilyCreate):
            Response<FamilyCreateBaseResponse>
    @DELETE("family")
    suspend fun deleteFamily(@Header("Authorization") token: String):
            Response<FamilyCreateBaseResponse>

    @POST("family/invite")
    suspend fun inviteUserToFamily(
        @Header("Authorization") token: String,
        @Header("username") username: String
    ): Response<FamilyInviteBaseResponse>

    @DELETE("family/leave")
    suspend fun leaveFamily(@Header("Authorization") token: String):
            Response<FamilyCreateBaseResponse>

    @DELETE("family/kick")
    suspend fun kickUser(@Header("Authorization") token: String, @Header("Id") id: Long):
            Response<FamilyInvitationsBaseResponse>

    @GET("family/invitations")
    suspend fun getInvitations(@Header("Authorization") token: String):
            Response<FamilyInvitationsBaseResponse>

    @DELETE("family/invitations/cancel")
    suspend fun cancelInvitation(@Header("Authorization") token: String, @Header("Id") id: Long):
            Response<FamilyInvitationsBaseResponse>

    @POST("family/invitations/accept")
    suspend fun acceptInvitation(@Header("Authorization") token: String, @Header("Id") id: Long):
            Response<FamilyInvitationsBaseResponse>
}