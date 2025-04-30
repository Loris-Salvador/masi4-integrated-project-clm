package be.hepl.clm.presentation.home


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.hepl.clm.data.article.ArticleRepository
import be.hepl.clm.domain.Article
import be.hepl.clm.domain.CartManager
import be.hepl.clm.domain.Category
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: ArticleRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<ArticleUiState>(ArticleUiState.Loading)
    val uiState: StateFlow<ArticleUiState> = _uiState.asStateFlow()

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories = _categories.asStateFlow()

    private val _selectedCategoryId = MutableStateFlow<Int?>(null)
    val selectedCategoryId = _selectedCategoryId.asStateFlow()

    private val _selectedCategoryName = MutableStateFlow<String?>(null)
    val selectedCategoryName = _selectedCategoryName.asStateFlow()

    private val _selectedArticle = MutableStateFlow<Article?>(null)
    val selectedArticle = _selectedArticle.asStateFlow()

    val cartManager = CartManager.getInstance()

    init {
        loadCategories()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            try {
                val categoriesList = repository.getAllCategories()
                _categories.value = categoriesList

                // Sélectionner automatiquement la première catégorie si la liste n'est pas vide
                if (categoriesList.isNotEmpty()) {
                    val firstCategory = categoriesList.first()
                    selectCategory(firstCategory)
                }
            } catch (e: Exception) {
                _uiState.value = ArticleUiState.Error("Erreur lors du chargement des catégories: ${e.message}")
            }
        }
    }

    fun loadArticles() {
        viewModelScope.launch {
            _uiState.value = ArticleUiState.Loading
            try {
                val articles = repository.getArticlesByCategory(_selectedCategoryId.value)
                _uiState.value = ArticleUiState.Success(articles)
            } catch (e: Exception) {
                _uiState.value = ArticleUiState.Error("Erreur lors du chargement des articles: ${e.message}")
            }
        }
    }

    fun selectCategory(category: Category?) {
        if (category == null) {
            _selectedCategoryId.value = null
            _selectedCategoryName.value = null
        } else {
            _selectedCategoryId.value = category.id_category
            _selectedCategoryName.value = category.category
        }
        loadArticles()
    }

    fun getFilteredArticles(): List<Article> {
        val currentState = _uiState.value
        return if (currentState is ArticleUiState.Success) {
            Log.d("debug", "articles = ${currentState.articles}")
            currentState.articles
        } else {
            emptyList()
        }
    }

    fun selectArticle(article: Article) {
        _selectedArticle.value = article
    }

    fun clearSelectedArticle() {
        _selectedArticle.value = null
    }

    fun addToCart(article: Article, quantity: Int = 1) {
        cartManager.addItem(article, quantity)
    }

    fun getCategoryNames(): List<String> {
        return _categories.value.map { it.category }
    }

    fun getCategoryByName(name: String): Category? {
        return _categories.value.find { it.category == name }
    }
}