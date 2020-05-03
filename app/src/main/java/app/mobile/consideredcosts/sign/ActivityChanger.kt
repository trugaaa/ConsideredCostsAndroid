package app.mobile.consideredcosts.sign

interface ActivityChanger {
    fun invokePinActivity()
    fun invokeGeneralErrorActivity(errorText: String)
}