package app.mobile.consideredcosts.http.models

import app.mobile.consideredcosts.http.BaseResponse

/**
 * Family structure
 */
class FamilyBaseResponse : BaseResponse<Family>()

data class Family(
    val Creator: String,
    val Name: String,
    val CreatedAt: String,
    val Money: Double,
    val members: MutableList<FamilyMember>
)

data class FamilyMember(
    val Id: Long,
    val role: String,
    val Money: Double,
    val Nickname: String?,
    val FirstName: String?,
    val SecondName: String,
    val Email: String
)

/**
 * Family invite answer structure
 */
class FamilyInviteBaseResponse : BaseResponse<FamilyInviteResponse>()

data class FamilyInviteResponse(
    val Id: Long,
    val FamilyId: Long,
    val UserName: String,
    val Date: String
)

/**
 * Family invitations structure
 */
class  FamilyInvitationsBaseResponse :  BaseResponse<FamilyInvitationResponse>()

data class FamilyInvitationResponse(val list: MutableList<FamilyInvitation>?)

data class FamilyInvitation(
    val Id: Long,
    val FamilyId: Long,
    val Username: String,
    val Date: String
)

/**
 * Family creation structure
 */
class FamilyCreateBaseResponse : BaseResponse<FamilyCreate>()

data class FamilyCreate(
    val Id: Long?,
    val Name: String,
    val AdditionalInfo: String?
)