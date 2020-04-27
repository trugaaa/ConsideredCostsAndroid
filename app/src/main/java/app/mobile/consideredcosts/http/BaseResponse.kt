package app.mobile.consideredcosts.http

import com.google.gson.annotations.SerializedName

open class BaseResponse<out T>(
    @SerializedName("data")
    val data: T? = null,
    @SerializedName("messages")
    val messages: List<ErrorResponse>? = null)
{
    val firstMessage: String?
        get() {
            return messages!!.first().Text
        }

    fun isMessageExists() = messages != null && messages.isNotEmpty()
}

data class ErrorResponse(
    val Text: String?,
    val Field: String?
)