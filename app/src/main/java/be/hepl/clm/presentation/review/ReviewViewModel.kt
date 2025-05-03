package be.hepl.clm.presentation.review

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.hepl.clm.data.review.ReviewRepository
import be.hepl.clm.data.review.ReviewService
import be.hepl.clm.domain.Review
import be.hepl.clm.domain.ReviewRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReviewViewModel @Inject constructor(
    private val reviewRepository: ReviewRepository,
)    : ViewModel() {

    private val _reviewsState = MutableStateFlow<ReviewsState>(ReviewsState.Loading)
    val reviewsState: StateFlow<ReviewsState> = _reviewsState.asStateFlow()

    private val _postReviewState = MutableStateFlow<PostReviewState>(PostReviewState.Idle)
    val postReviewState: StateFlow<PostReviewState> = _postReviewState.asStateFlow()

    private val _userRating = MutableStateFlow(0)
    val userRating: StateFlow<Int> = _userRating.asStateFlow()

    private val _userComment = MutableStateFlow("")
    val userComment: StateFlow<String> = _userComment.asStateFlow()

    private val _username = MutableStateFlow("")
    val username: StateFlow<String> = _username.asStateFlow()

    fun loadReviews(productId: Int) {
        _reviewsState.value = ReviewsState.Loading

        viewModelScope.launch {
            reviewRepository.getReviews(productId).fold(
                onSuccess = { reviews ->
                    _reviewsState.value = ReviewsState.Success(reviews)
                },
                onFailure = { exception ->
                    _reviewsState.value =
                        ReviewsState.Error("Erreur: ${exception.localizedMessage}")
                }
            )
        }
    }

    fun postReview(productId: Int) {
        if (_username.value.isBlank()) {
            _postReviewState.value = PostReviewState.Error("Veuillez entrer votre nom")
            return
        }

        if (_userRating.value == 0) {
            _postReviewState.value = PostReviewState.Error("Veuillez attribuer une note")
            return
        }

        _postReviewState.value = PostReviewState.Loading

        val reviewRequest = ReviewRequest(
            productId = productId,
            username = _username.value,
            rating = _userRating.value,
            comment = _userComment.value
        )

        viewModelScope.launch {
            reviewRepository.postReview(reviewRequest).fold(
                onSuccess = {
                    _postReviewState.value = PostReviewState.Success

                    _userRating.value = 0
                    _userComment.value = ""

                    loadReviews(productId)
                },
                onFailure = { exception ->
                    _postReviewState.value =
                        PostReviewState.Error("Erreur: ${exception.localizedMessage}")
                }
            )
        }
    }

    fun updateUsername(name: String) {
        _username.value = name
    }

    fun updateRating(rating: Int) {
        _userRating.value = rating
    }

    fun updateComment(comment: String) {
        _userComment.value = comment
    }

    sealed class ReviewsState {
        object Loading : ReviewsState()
        data class Success(val reviews: List<Review>) : ReviewsState()
        data class Error(val message: String) : ReviewsState()
    }

    sealed class PostReviewState {
        object Idle : PostReviewState()
        object Loading : PostReviewState()
        object Success : PostReviewState()
        data class Error(val message: String) : PostReviewState()
    }
}