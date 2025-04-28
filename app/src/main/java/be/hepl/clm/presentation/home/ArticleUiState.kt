package be.hepl.clm.presentation.home

import be.hepl.clm.domain.Article


sealed class ArticleUiState {
    data object Loading : ArticleUiState()
    data class Success(val articles: List<Article>) : ArticleUiState()
    data class Error(val message: String) : ArticleUiState()
}