package be.hepl.clm.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.hepl.clm.data.article.ArticleRepository
import be.hepl.clm.domain.Article
import be.hepl.clm.domain.CartManager
import be.hepl.clm.domain.Category
import be.hepl.clm.domain.CategoryTax
import be.hepl.clm.domain.Picture
import be.hepl.clm.domain.Stock
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel() : ViewModel() {
    private val _uiState = MutableStateFlow<ArticleUiState>(ArticleUiState.Loading)
    val uiState: StateFlow<ArticleUiState> = _uiState.asStateFlow()

    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory = _selectedCategory.asStateFlow()

    private val _categories = MutableStateFlow<List<String>>(emptyList())
    val categories = _categories.asStateFlow()

    // Pour la navigation vers les détails
    private val _selectedArticle = MutableStateFlow<Article?>(null)
    val selectedArticle = _selectedArticle.asStateFlow()

    // CartManager instance
    val cartManager = CartManager.getInstance()

    init {
        loadArticles()
    }

    fun loadArticles() {
        viewModelScope.launch {
            _uiState.value = ArticleUiState.Loading
            try {
                val articles = getHardcodedArticles();

                // Extraire toutes les catégories uniques
                val uniqueCategories = articles.map { it.category.category }.distinct()
                _categories.value = uniqueCategories

                _uiState.value = ArticleUiState.Success(articles)
            } catch (e: Exception) {
                _uiState.value = ArticleUiState.Error("Erreur lors du chargement des articles: ${e.message}")
            }
        }
    }

    fun selectCategory(category: String?) {
        _selectedCategory.value = category
    }

    fun getFilteredArticles(): List<Article> {
        val currentState = _uiState.value
        return if (currentState is ArticleUiState.Success) {
            if (_selectedCategory.value == null) {
                currentState.articles
            } else {
                currentState.articles.filter { it.category.category == _selectedCategory.value }
            }
        } else {
            emptyList()
        }
    }

    // Nouvelle fonction pour sélectionner un article et naviguer vers ses détails
    fun selectArticle(article: Article) {
        _selectedArticle.value = article
    }

    // Fonction pour effacer l'article sélectionné (après navigation)
    fun clearSelectedArticle() {
        _selectedArticle.value = null
    }

    // Fonction d'ajout rapide au panier depuis la HomeScreen
    fun addToCart(article: Article, quantity: Int = 1) {
        cartManager.addItem(article, quantity)
    }
}


private fun getHardcodedArticles(): List<Article> {
    return listOf(
        Article(
            id = 1,
            name = "Echo Dot (5th generation, 2022 release) Big vibrant sound Wi-Fi and Bluetooth smart speaker with Alexa Charcoal",
            description = "Erreur volonté tâche fin supposer droit.",
            category = Category(
                id_category = 57,
                category = "Hi-Fi Speakers",
                description = "Consentir immobile médecin détacher."
            ),
            price = 21.99,
            promotion = 15,
            pictures = emptyList(),
            categoryTax = CategoryTax(
                category = 3,
                description = "Le plus commun qui correspond à tout le reste",
                percentage = 21
            ),
            stock = Stock(
                stockId = 1,
                location = "LK",
                quantity = 469
            ),
            picture = emptyList()
        ),
        Article(
            id = 2,
            name = "Anker Soundcore mini, Super-Portable Bluetooth Speaker with 15-Hour Playtime",
            description = "Haut choix chercher.",
            category = Category(
                id_category = 57,
                category = "Hi-Fi Speakers",
                description = "Consentir immobile médecin détacher."
            ),
            price = 23.99,
            promotion = 10,
            pictures = listOf(
                Picture(
                    id = 10685,
                    path = "https://masimart.s3.eu-north-1.amazonaws.com/1744275855855.jpg",
                    description = ""
                )
            ),
            categoryTax = CategoryTax(
                category = 3,
                description = "Le plus commun qui correspond à tout le reste",
                percentage = 21
            ),
            stock = Stock(
                stockId = 2,
                location = "MY",
                quantity = 393
            ),
            picture = listOf(
                Picture(
                    id = 10685,
                    path = "https://masimart.s3.eu-north-1.amazonaws.com/1744275855855.jpg",
                    description = ""
                )
            )
        ),
        Article(
            id = 3,
            name = "Echo Dot (5th generation, 2022 release) Big vibrant sound Wi-Fi and Bluetooth smart speaker with Alexa Deep Sea Blue",
            description = "Sein cuisine promettre puis céder.",
            category = Category(
                id_category = 57,
                category = "Hi-Fi Speakers",
                description = "Consentir immobile médecin détacher."
            ),
            price = 21.99,
            promotion = 5,
            pictures = listOf(
                Picture(
                    id = 2068,
                    path = "https://masimart.s3.eu-north-1.amazonaws.com/1744275855855.jpg",
                    description = "Conduire doux joindre tu vers précipiter."
                ),
                Picture(
                    id = 14567,
                    path = "https://masimart.s3.eu-north-1.amazonaws.com/1744275855855.jpg",
                    description = ""
                )
            ),
            categoryTax = CategoryTax(
                category = 3,
                description = "Le plus commun qui correspond à tout le reste",
                percentage = 21
            ),
            stock = Stock(
                stockId = 3,
                location = "DZ",
                quantity = 349
            ),
            picture = listOf(
                Picture(
                    id = 2068,
                    path = "https://masimart.s3.eu-north-1.amazonaws.com/1744275855855.jpg",
                    description = "Conduire doux joindre tu vers précipiter."
                ),
                Picture(
                    id = 14567,
                    path = "https://masimart.s3.eu-north-1.amazonaws.com/1744275855855.jpg",
                    description = ""
                )
            )
        ),
        Article(
            id = 4,
            name = "Sony WH-1000XM4 Wireless Noise Cancelling Headphones",
            description = "Casque à réduction de bruit sans fil premium.",
            category = Category(
                id_category = 58,
                category = "Headphones",
                description = "Casques audio de haute qualité"
            ),
            price = 349.99,
            promotion = 20,
            pictures = listOf(
                Picture(
                    id = 3456,
                    path = "https://masimart.s3.eu-north-1.amazonaws.com/1744275855855.jpg",
                    description = "Sony WH-1000XM4 Black"
                )
            ),
            categoryTax = CategoryTax(
                category = 3,
                description = "Le plus commun qui correspond à tout le reste",
                percentage = 21
            ),
            stock = Stock(
                stockId = 4,
                location = "FR",
                quantity = 120
            ),
            picture = listOf(
                Picture(
                    id = 3456,
                    path = "https://masimart.s3.eu-north-1.amazonaws.com/1744275855855.jpg",
                    description = "Sony WH-1000XM4 Black"
                )
            )
        ),
        Article(
            id = 5,
            name = "Apple AirPods Pro (2nd generation)",
            description = "Écouteurs sans fil avec réduction active du bruit.",
            category = Category(
                id_category = 58,
                category = "Headphones",
                description = "Casques audio de haute qualité"
            ),
            price = 249.99,
            promotion = 0,
            pictures = listOf(
                Picture(
                    id = 7890,
                    path = "https://masimart.s3.eu-north-1.amazonaws.com/1744275855855.jpg",
                    description = "AirPods Pro in charging case"
                )
            ),
            categoryTax = CategoryTax(
                category = 3,
                description = "Le plus commun qui correspond à tout le reste",
                percentage = 21
            ),
            stock = Stock(
                stockId = 5,
                location = "US",
                quantity = 15
            ),
            picture = listOf(
                Picture(
                    id = 7890,
                    path = "https://masimart.s3.eu-north-1.amazonaws.com/1744275855855.jpg",
                    description = "AirPods Pro in charging case"
                )
            )
        ),
        Article(
            id = 6,
            name = "Samsung Galaxy Buds Pro",
            description = "Écouteurs true wireless avec réduction de bruit.",
            category = Category(
                id_category = 58,
                category = "Headphones",
                description = "Casques audio de haute qualité"
            ),
            price = 199.99,
            promotion = 25,
            pictures = listOf(
                Picture(
                    id = 1234,
                    path = "https://masimart.s3.eu-north-1.amazonaws.com/1744275855855.jpg",
                    description = "Galaxy Buds Pro Black"
                )
            ),
            categoryTax = CategoryTax(
                category = 3,
                description = "Le plus commun qui correspond à tout le reste",
                percentage = 21
            ),
            stock = Stock(
                stockId = 6,
                location = "KR",
                quantity = 0
            ),
            picture = listOf(
                Picture(
                    id = 1234,
                    path = "https://masimart.s3.eu-north-1.amazonaws.com/1744275855855.jpg",
                    description = "Galaxy Buds Pro Black"
                )
            )
        )
    )
}