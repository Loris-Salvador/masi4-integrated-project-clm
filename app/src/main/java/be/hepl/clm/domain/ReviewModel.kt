package be.hepl.clm.domain

data class Review(
    val id: Int,
    val productId: Int,
    val username: String,
    val rating: Int,
    val comment: String,
    val date: String
)

data class ReviewRequest(
    val productId: Int,
    val username: String,
    val rating: Int,
    val comment: String
)

