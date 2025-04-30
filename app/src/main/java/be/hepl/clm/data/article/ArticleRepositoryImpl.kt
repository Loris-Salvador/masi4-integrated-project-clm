package be.hepl.clm.data.article

import be.hepl.clm.domain.Article
import be.hepl.clm.domain.Category
import javax.inject.Inject

class ArticleRepositoryImpl @Inject constructor(
    private val articleService: ArticleService
) : ArticleRepository {

    override suspend fun getAllCategories(): List<Category> {
        return articleService.getAllCategories()
    }

    override suspend fun getArticlesByCategory(categoryId: Int?): List<Article> {
        return articleService.getArticlesByCategory(categoryId)
    }
}