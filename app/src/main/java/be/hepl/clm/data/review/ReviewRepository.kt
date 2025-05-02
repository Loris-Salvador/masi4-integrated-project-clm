package be.hepl.clm.data.review

import be.hepl.clm.data.purchase.PurchaseApiService
import be.hepl.clm.data.token.TokenRepository
import be.hepl.clm.domain.Review
import be.hepl.clm.domain.ReviewRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Repository pour gérer les opérations liées aux commentaires d'articles
 */
class ReviewRepository@Inject constructor(
    private val reviewService: ReviewService,
) {

    /**
     * Récupère les commentaires pour un article spécifique
     * @param productId ID de l'article
     * @return Liste des commentaires ou null en cas d'erreur
     */
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

    /**
     * Envoie un nouveau commentaire
     * @param reviewRequest Données du commentaire à envoyer
     * @return Le commentaire créé ou null en cas d'erreur
     */
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