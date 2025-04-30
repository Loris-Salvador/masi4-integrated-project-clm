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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import be.hepl.clm.domain.Article
import be.hepl.clm.domain.CartManager
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticleDetailScreen(
    article: Article,
    onNavigateBack: () -> Unit
) {
    val cartManager = CartManager.getInstance()
    var quantity by remember { mutableIntStateOf(1) }
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Détails du produit") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Retour")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState)
        ) {
            // Image principale
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
                    Text("Pas d'image disponible")
                }
            }

            // Informations sur l'article
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

                // Nom
                Text(
                    text = article.name,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                // Prix avec promotion si applicable
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    if (article.promotion > 0) {
                        // Prix barré
                        Text(
                            text = "${article.price}€",
                            style = MaterialTheme.typography.bodyLarge,
                            textDecoration = TextDecoration.LineThrough,
                            color = Color.Gray
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        // Nouveau prix
                        val newPrice = article.price * (1 - article.promotion / 100.0)
                        Text(
                            text = String.format("%.2f€", newPrice),
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.error
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        // Badge promotion
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
                        // Prix normal
                        Text(
                            text = "${article.price}€",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Description
                Text(
                    text = "Description",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                )

                Text(
                    text = article.description,
                    style = MaterialTheme.typography.bodyMedium
                )

                // Disponibilité
                val stockColor = when {
                    article.stock.quantity > 100 -> Color.Green
                    article.stock.quantity > 20 -> Color(0xFFFFA500) // Orange
                    article.stock.quantity > 0 -> Color.Red
                    else -> Color.Gray
                }

                Text(
                    text = "Disponibilité",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                )

                Text(
                    text = when {
                        article.stock.quantity > 100 -> "En stock (${article.stock.quantity} disponibles)"
                        article.stock.quantity > 0 -> "Stock limité (${article.stock.quantity} disponibles)"
                        else -> "Rupture de stock"
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = stockColor
                )

                // Contrôle de quantité et bouton d'achat
                if (article.stock.quantity > 0) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 24.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Quantité:",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(end = 16.dp)
                        )

                        // Contrôles de quantité
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

                    // Bouton Ajouter au panier
                    Button(
                        onClick = {
                            cartManager.addItem(article, quantity)
                            // Message de confirmation optionnel
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
                        Text("Ajouter au panier")
                    }
                } else {
                    // Message de rupture de stock
                    Button(
                        onClick = { /* Potentiellement gérer une notification quand disponible */ },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        enabled = false
                    ) {
                        Text("Produit indisponible")
                    }
                }
            }
        }
    }
}