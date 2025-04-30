package be.hepl.clm.data.article

import be.hepl.clm.domain.Article
import be.hepl.clm.domain.Category

interface ArticleRepository {
    suspend fun getAllCategories(): List<Category>

    suspend fun getArticlesByCategory(categoryId: Int? = null): List<Article>
}