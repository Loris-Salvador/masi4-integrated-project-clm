package be.hepl.clm.presentation.home

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import be.hepl.clm.domain.Article
import be.hepl.clm.domain.Category
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: HomeViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val selectedCategoryName by viewModel.selectedCategoryName.collectAsState()
    val categories by viewModel.categories.collectAsState()
    val selectedArticle by viewModel.selectedArticle.collectAsState()
    val cartItems by viewModel.cartManager.items.collectAsState()
    val cartItemCount by viewModel.cartManager.totalItems.collectAsState()

    // Si un article est sélectionné, afficher l'écran de détails
    if (selectedArticle != null) {
        ArticleDetailScreen(
            article = selectedArticle!!,
            onNavigateBack = { viewModel.clearSelectedArticle() }
        )
        return
    }

    // Écran principal avec panier
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nos produits") },
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            // Filtres par catégorie
            CategoryFilter(
                categories = categories,
                selectedCategoryName = selectedCategoryName,
                onCategorySelected = { viewModel.selectCategory(it) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Contenu principal
            when (uiState) {
                is ArticleUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                is ArticleUiState.Success -> {
                    val filteredArticles = viewModel.getFilteredArticles()

                    Log.d("jsp", viewModel.getCategoryNames().toString())

                    if (selectedCategoryName == null) {
                        // Message à afficher quand aucune catégorie n'est sélectionnée
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Choisissez une catégorie",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    } else if (filteredArticles.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Aucun article trouvé dans cette catégorie")
                        }
                    } else {
                        Log.d("jsp", "articles list")

                        ArticleList(
                            articles = filteredArticles,
                            onArticleClick = { viewModel.selectArticle(it) },
                            onAddToCartClick = { viewModel.addToCart(it) }
                        )
                    }
                }
                is ArticleUiState.Error -> {
                    val errorState = uiState as ArticleUiState.Error
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Erreur",
                                style = MaterialTheme.typography.headlineSmall,
                                color = MaterialTheme.colorScheme.error
                            )
                            Text(
                                text = errorState.message,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.error
                            )
                            Button(
                                onClick = { viewModel.loadArticles() },
                                modifier = Modifier.padding(top = 16.dp)
                            ) {
                                Text("Réessayer")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryFilter(
    categories: List<Category>,
    selectedCategoryName: String?,
    onCategorySelected: (Category?) -> Unit
) {
    Column {
        Text(
            text = "Filtrer par catégorie",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {

            // Catégories
            items(categories) { category ->
                FilterChip(
                    selected = selectedCategoryName == category.category,
                    onClick = { onCategorySelected(category) },
                    label = { Text(category.category) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
        }
    }
}

@Composable
fun ArticleList(
    articles: List<Article>,
    onArticleClick: (Article) -> Unit,
    onAddToCartClick: (Article) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(articles) { article ->
            ArticleCard(
                article = article,
                onArticleClick = { onArticleClick(article) },
                onAddToCartClick = { onAddToCartClick(article) }
            )
        }
    }
}

@Composable
fun ArticleCard(
    article: Article,
    onArticleClick: () -> Unit,
    onAddToCartClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onArticleClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {

            // Image de l'article avec bouton d'ajout au panier
            Box(modifier = Modifier.fillMaxWidth()) {
                if (article.pictures.isNotEmpty()) {
                    AsyncImage(
                        model = article.pictures.first().path,
                        contentDescription = article.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    // Placeholder si pas d'image
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .background(Color.LightGray)
                            .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Pas d'image disponible",
                            color = Color.White
                        )
                    }
                }

                // Bouton d'ajout rapide au panier
                if (article.stock.quantity > 0) {
                    FloatingActionButton(
                        onClick = { onAddToCartClick() },
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(12.dp)
                            .size(40.dp),
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Ajouter au panier",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            // Badge promotion si promotion > 0
            if (article.promotion > 0) {
                Surface(
                    color = MaterialTheme.colorScheme.error,
                    shape = RoundedCornerShape(bottomEnd = 12.dp),
                    modifier = Modifier
                        .offset((-12).dp, (-12).dp)
                        .padding(start = 12.dp)
                ) {
                    Text(
                        text = "-${article.promotion}%",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            // Détails de l'article
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Catégorie
                Text(
                    text = article.category.category,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )

                // Nom de l'article
                Text(
                    text = article.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(vertical = 4.dp)
                )

                // Description
                Text(
                    text = article.description,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Prix et disponibilité
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Prix avec promotion si applicable
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (article.promotion > 0) {
                            // Prix barré
                            Text(
                                text = "${article.price}€",
                                style = MaterialTheme.typography.bodyMedium,
                                textDecoration = TextDecoration.LineThrough,
                                color = Color.Gray
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            // Nouveau prix
                            val newPrice = article.price * (1 - article.promotion / 100.0)
                            Text(
                                text = String.format("%.2f€", newPrice),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.error
                            )
                        } else {
                            // Prix normal
                            Text(
                                text = "${article.price}€",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // Indicateur de stock
                    val stockColor = when {
                        article.stock.quantity > 100 -> Color.Green
                        article.stock.quantity > 20 -> Color(0xFFFFA500) // Orange
                        else -> Color.Red
                    }

                    Text(
                        text = if (article.stock.quantity > 0) "En stock" else "Rupture",
                        style = MaterialTheme.typography.bodySmall,
                        color = stockColor
                    )
                }
            }
        }
    }
}