package be.hepl.clm.data.auth

data class ChallengeRequest(
    val email: String,
    val challenge: String
)
