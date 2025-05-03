package be.hepl.clm.data.review

import be.hepl.clm.domain.Review
import be.hepl.clm.domain.ReviewRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


class ReviewRepository@Inject constructor(
    private val reviewService: ReviewService,
) {

    suspend fun getReviews(productId: Int): Result<List<Review>> = withContext(Dispatchers.IO) {
        try {
            val response = reviewService.getReviews(productId)

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Erreur lors du chargement des commentaires: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun postReview(reviewRequest: ReviewRequest): Result<Review> = withContext(Dispatchers.IO) {
        try {
            val response = reviewService.postReview(reviewRequest)

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Erreur lors de l'envoi du commentaire: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}