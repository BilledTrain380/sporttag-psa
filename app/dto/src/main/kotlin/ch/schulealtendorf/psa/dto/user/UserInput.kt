package ch.schulealtendorf.psa.dto.user

data class UserInput(
    val username: String,
    val enabled: Boolean,
    val password: String
)