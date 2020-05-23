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
            messages.first().text ?: "Something went wrong"
        }
    }

    fun isMessageExists() = messages != null && messages.isNotEmpty()

//todo for future fix
    //
    //
//    fun errorMessageGet(response: Response<BaseResponse<LoginRequestResponse>>): String? {
//        val gson = Gson()
//        val adapter = gson.getAdapter(BaseResponse::class.java)
//        var result: String? = null
//        try {
//            if (response.errorBody() != null)
//                result = adapter.fromJson(
//                    response.errorBody()!!.string()
//                ).firstMessage()
//
//
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//       return result
//    }
}

    data class ErrorResponse(
        @SerializedName("text")
        val text: String?,
        @SerializedName("field")
        val field: String?
    )