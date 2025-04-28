package be.hepl.clm.domain

import kotlinx.datetime.Instant

data class CustomerSignupDTO(
    val email: String,
    val password: String,
    val phoneNumber: String,
    val name: String,
    val lastName: String,
    val gender: Gender,
    val birthday: Instant?
)
