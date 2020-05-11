package app.mobile.consideredcosts.basic

class FieldValidator {
    fun isPasswordValid(password: String): Boolean {
        return password.matches(VALID_PASSWORD)
    }

    fun isEmailValid(emailAddress: String): Boolean {
        return emailAddress.matches(VALID_EMAIL_ADDRESS)
    }

    fun isUsernameValid(username: String): Boolean {
        return username.matches(VALID_USERNAME)
    }

    companion object {
        private val VALID_PASSWORD = Regex("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,255}\$")
        private val VALID_EMAIL_ADDRESS =
            ("^(([a-zA-Z])+([\\-._])?\\w*){3,64}@{1}(([a-zA-Z])+\\-?\\w*\\.([a-zA-Z]){2,3})\$").toRegex()
        private val VALID_USERNAME = ("^([A-Za-z0-9_.-]){3,64}\$").toRegex()
    }


}