package ch.schulealtendorf.sporttagpsa.controller.authorization

data class ChangePasswordForm(
    val password: String,
    val passwordRepeat: String
)
