package be.hepl.clm.data.review

import be.hepl.clm.domain.Review
import be.hepl.clm.domain.ReviewRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ReviewService {

    @POST("reviews")
    suspend fun postReview(@Body reviewRequest: ReviewRequest): Response<Review>

    @GET("reviews/{productId}")
    suspend fun getReviews(@Path("productId") productId: Int): Response<List<Review>>
}