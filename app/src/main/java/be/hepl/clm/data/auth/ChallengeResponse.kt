package be.hepl.clm.data.auth

data class ChallengeResponse(
    val accessToken: String,
    val refreshToken: String
)
