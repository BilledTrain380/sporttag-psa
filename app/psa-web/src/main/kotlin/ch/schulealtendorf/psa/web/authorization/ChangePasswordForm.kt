package ch.schulealtendorf.psa.web.authorization

data class ChangePasswordForm(
    val password: String,
    val passwordRepeat: String
)
