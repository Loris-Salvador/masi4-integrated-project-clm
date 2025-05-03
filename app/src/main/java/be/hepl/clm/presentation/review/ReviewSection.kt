package be.hepl.clm.presentation.review

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import be.hepl.clm.R
import be.hepl.clm.domain.Review

@Composable
fun ReviewSection(
    productId: Int,
    viewModel: ReviewViewModel = hiltViewModel()
) {
    val reviewsState by viewModel.reviewsState.collectAsState()
    val postReviewState by viewModel.postReviewState.collectAsState()
    val userRating by viewModel.userRating.collectAsState()
    val userComment by viewModel.userComment.collectAsState()
    val username by viewModel.username.collectAsState()

    LaunchedEffect(productId) {
        viewModel.loadReviews(productId)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        Text(
            text = stringResource(R.string.review_section_title),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        AddReviewForm(
            username = username,
            rating = userRating,
            comment = userComment,
            onUsernameChange = { viewModel.updateUsername(it) },
            onRatingChange = { viewModel.updateRating(it) },
            onCommentChange = { viewModel.updateComment(it) },
            onSubmit = { viewModel.postReview(productId) },
            postState = postReviewState
        )

        Spacer(modifier = Modifier.height(16.dp))
        Divider()
        Spacer(modifier = Modifier.height(16.dp))

        when (reviewsState) {
            is ReviewViewModel.ReviewsState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is ReviewViewModel.ReviewsState.Success -> {
                val reviews = (reviewsState as ReviewViewModel.ReviewsState.Success).reviews
                if (reviews.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.no_reviews),
                            style = MaterialTheme.typography.bodyMedium,
                            fontStyle = FontStyle.Italic
                        )
                    }
                } else {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        reviews.forEach { review ->
                            ReviewItem(review = review)
                            Divider(modifier = Modifier.padding(vertical = 8.dp))
                        }
                    }
                }
            }
            is ReviewViewModel.ReviewsState.Error -> {
                val errorMessage = (reviewsState as ReviewViewModel.ReviewsState.Error).message
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = errorMessage,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@Composable
fun AddReviewForm(
    username: String,
    rating: Int,
    comment: String,
    onUsernameChange: (String) -> Unit,
    onRatingChange: (Int) -> Unit,
    onCommentChange: (String) -> Unit,
    onSubmit: () -> Unit,
    postState: ReviewViewModel.PostReviewState
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(R.string.review_form_title),
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = username,
            onValueChange = onUsernameChange,
            label = { Text(stringResource(R.string.your_name)) },
            leadingIcon = {
                Icon(Icons.Default.Person, contentDescription = stringResource(R.string.name_icon_description))
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = stringResource(R.string.your_rating),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(end = 16.dp)
            )

            StarRating(
                currentRating = rating,
                onRatingChanged = onRatingChange,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = comment,
            onValueChange = onCommentChange,
            label = { Text(stringResource(R.string.your_comment_optional)) },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            maxLines = 5
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (postState is ReviewViewModel.PostReviewState.Error) {
            Text(
                text = postState.message,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        Button(
            onClick = onSubmit,
            modifier = Modifier.align(Alignment.End),
            enabled = postState !is ReviewViewModel.PostReviewState.Loading
        ) {
            if (postState is ReviewViewModel.PostReviewState.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.dp
                )
            } else {
                Text(stringResource(R.string.send_review))
            }
        }

        if (postState is ReviewViewModel.PostReviewState.Success) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.review_success),
                style = MaterialTheme.typography.bodySmall,
                color = Color.Green,
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}

@Composable
fun StarRating(
    currentRating: Int,
    onRatingChanged: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        for (i in 1..5) {
            Icon(
                imageVector = if (i <= currentRating) Icons.Default.Star else Icons.Default.StarBorder,
                contentDescription = stringResource(R.string.star_icon_description, i),
                tint = if (i <= currentRating) Color(0xFFFFC107) else Color.Gray,
                modifier = Modifier
                    .size(28.dp)
                    .clickable { onRatingChanged(i) }
                    .padding(2.dp)
            )
        }
    }
}

@Composable
fun ReviewItem(review: Review) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = stringResource(R.string.avatar_icon_description),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column {
                Text(
                    text = review.username,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "", // future date?
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Row {
                for (i in 1..5) {
                    Icon(
                        imageVector = if (i <= review.rating) Icons.Default.Star else Icons.Default.StarBorder,
                        contentDescription = null,
                        tint = if (i <= review.rating) Color(0xFFFFC107) else Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        if (review.comment.isNotBlank()) {
            Text(
                text = review.comment,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(start = 48.dp, top = 8.dp)
            )
        }
    }
}
