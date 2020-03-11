package app.mobile.consideredcosts.sign

interface ActivityChanger {
    fun invokeMainActivity()
    fun invokeGeneralErrorActivity(errorText: MutableList<String>)
}