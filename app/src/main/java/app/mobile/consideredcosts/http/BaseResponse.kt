package app.mobile.consideredcosts.http

import com.google.gson.annotations.SerializedName

open class BaseResponse<out T>(
    @SerializedName("data")
    val data: T? = null,
    @SerializedName("messages")
    val messages: List<ErrorResponse>? = null
) {

    fun firstMessage(): String {
        return if (messages.isNullOrEmpty()) {
            "Messages list is null or empty"
        } else {
            messages.first()?.text ?: "Something went wrong"
        }
    }

    fun isMessageExists() = messages != null && messages.isNotEmpty()
}

data class ErrorResponse(
    val text: String?,
    val field: String?
)