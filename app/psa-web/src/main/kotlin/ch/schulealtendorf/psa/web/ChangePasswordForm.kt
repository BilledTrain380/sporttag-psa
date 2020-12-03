package ch.schulealtendorf.psa.web

data class ChangePasswordForm(
    val password: String,
    val passwordRepeat: String
)
