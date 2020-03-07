package app.mobile.consideredcosts.http

import com.google.gson.annotations.SerializedName

open class BaseResponse<out T>(
    @SerializedName("data")
    val data: T? = null,
    @SerializedName("messages")
    val messages: List<ErrorResponse>? = null)
{
    val message: String?
        get() {
            return messages!!.first().text
        }

    fun isMessageExists() = messages != null && messages.isNotEmpty()
}

data class ErrorResponse(
    val type: String?,
    val text: String?,
    val field: String?
)