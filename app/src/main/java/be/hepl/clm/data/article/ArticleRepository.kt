package be.hepl.clm.data.article

import be.hepl.clm.domain.Article

interface ArticleRepository {
    suspend fun getArticles(): List<Article>
}