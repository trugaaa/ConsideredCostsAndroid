package app.mobile.consideredcosts.basic

class PasswordValidator {
        fun isPasswordValid(password: String): Boolean {
            return password.matches(VALID_PASSWORD)
        }

        companion object {
            private val VALID_PASSWORD =
                "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$"
                    .toRegex()
        }
    }
