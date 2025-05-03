package be.hepl.clm.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import be.hepl.clm.R
import be.hepl.clm.domain.Article
import be.hepl.clm.domain.CartManager
import be.hepl.clm.presentation.review.ReviewSection
import be.hepl.clm.presentation.review.ReviewViewModel
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticleDetailScreen(
    article: Article,
    onNavigateBack: () -> Unit,
    reviewViewModel: ReviewViewModel = hiltViewModel()
) {
    val cartManager = CartManager.getInstance()
    var quantity by remember { mutableIntStateOf(1) }
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.product_details_title)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = stringResource(R.string.back))
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
            ) {
                if (article.pictures.isNotEmpty()) {
                    AsyncImage(
                        model = article.pictures.first().path,
                        contentDescription = article.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp),
                        contentScale = ContentScale.Fit
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .background(Color.LightGray),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(stringResource(R.string.no_image_available))
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = article.category.category,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Text(
                        text = article.name,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 8.dp)
                    ) {
                        if (article.promotion > 0) {
                            Text(
                                text = "${article.price}€",
                                style = MaterialTheme.typography.bodyLarge,
                                textDecoration = TextDecoration.LineThrough,
                                color = Color.Gray
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            val newPrice = article.price * (1 - article.promotion / 100.0)
                            Text(
                                text = String.format("%.2f€", newPrice),
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.error
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Surface(
                                color = MaterialTheme.colorScheme.error,
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = "-${article.promotion}%",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        } else {
                            Text(
                                text = "${article.price}€",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Text(
                        text = stringResource(R.string.description),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                    )

                    Text(
                        text = article.description,
                        style = MaterialTheme.typography.bodyMedium
                    )

                    val stockColor = when {
                        article.stock.quantity > 100 -> Color.Green
                        article.stock.quantity > 20 -> Color(0xFFFFA500)
                        article.stock.quantity > 0 -> Color.Red
                        else -> Color.Gray
                    }

                    Text(
                        text = stringResource(R.string.availability),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                    )

                    Text(
                        text = when {
                            article.stock.quantity > 100 ->
                                stringResource(R.string.in_stock, article.stock.quantity)
                            article.stock.quantity > 0 ->
                                stringResource(R.string.limited_stock, article.stock.quantity)
                            else -> stringResource(R.string.out_of_stock)
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = stockColor
                    )

                    if (article.stock.quantity > 0) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 24.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(R.string.quantity),
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(end = 16.dp)
                            )

                            Button(
                                onClick = { if (quantity > 1) quantity-- },
                                modifier = Modifier.size(40.dp),
                                contentPadding = PaddingValues(0.dp),
                                enabled = quantity > 1
                            ) {
                                Text("-")
                            }

                            Text(
                                text = quantity.toString(),
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )

                            Button(
                                onClick = { if (quantity < article.stock.quantity) quantity++ },
                                modifier = Modifier.size(40.dp),
                                contentPadding = PaddingValues(0.dp),
                                enabled = quantity < article.stock.quantity
                            ) {
                                Text("+")
                            }
                        }

                        Button(
                            onClick = {
                                cartManager.addItem(article, quantity)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp),
                            enabled = article.stock.quantity > 0
                        ) {
                            Icon(
                                imageVector = Icons.Default.ShoppingCart,
                                contentDescription = null,
                                modifier = Modifier.padding(end = 8.dp)
                            )
                            Text(stringResource(R.string.add_to_cart))
                        }
                    } else {
                        Button(
                            onClick = {},
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp),
                            enabled = false
                        ) {
                            Text(stringResource(R.string.product_unavailable))
                        }
                    }

                    Divider(modifier = Modifier.padding(vertical = 16.dp))

                    ReviewSection(
                        productId = article.id,
                        viewModel = reviewViewModel
                    )
                }
            }
        }
    }
}
