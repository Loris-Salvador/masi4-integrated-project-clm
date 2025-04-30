package be.hepl.clm.data.article

import be.hepl.clm.domain.Article
import be.hepl.clm.domain.Category
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Inject
import javax.inject.Singleton

interface ApiService {
    @GET("/category/all")
    suspend fun getAllCategories(): List<Category>

    @GET("/product/all")
    suspend fun getProductsByCategory(@Query("category") categoryId: Int? = null): List<Article>
}


@Singleton
class ArticleService @Inject constructor(private val apiService: ApiService) {

    suspend fun getAllCategories(): List<Category> {
        return withContext(Dispatchers.IO) {
            try {
                apiService.getAllCategories()
            } catch (e: Exception) {
                e.printStackTrace()
                emptyList()
            }
        }
    }

    suspend fun getArticlesByCategory(categoryId: Int? = null): List<Article> {
        return withContext(Dispatchers.IO) {
            try {
                apiService.getProductsByCategory(categoryId)
            } catch (e: Exception) {
                e.printStackTrace()
                emptyList()
            }
        }
    }
}