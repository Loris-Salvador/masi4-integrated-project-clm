package be.hepl.clm.data.auth

data class LoginRequest(
    val email: String,
    val password: String
)
