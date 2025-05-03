package be.hepl.clm.di

import be.hepl.clm.data.article.ApiService
import be.hepl.clm.data.article.ArticleRepository
import be.hepl.clm.data.article.ArticleRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ArticleModule {

    @Binds
    @Singleton
    abstract fun bindArticleRepository(
        articleRepositoryImpl: ArticleRepositoryImpl
    ): ArticleRepository

    companion object {
        @Provides
        @Singleton
        fun provideApiService(@Named("ArticleAPI") retrofit: Retrofit): ApiService {
            return retrofit.create(ApiService::class.java)
        }
    }
}