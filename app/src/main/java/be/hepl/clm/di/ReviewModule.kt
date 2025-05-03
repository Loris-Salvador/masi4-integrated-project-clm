package be.hepl.clm.di

import be.hepl.clm.data.review.ReviewRepository
import be.hepl.clm.data.review.ReviewService
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
abstract class ReviewModule {

    companion object {

        @Provides
        @Singleton
        fun provideReviewService(@Named("Chat") retrofit: Retrofit): ReviewService {
            return retrofit.create(ReviewService::class.java)
        }

        @Provides
        fun provideReviewRepository(api: ReviewService): ReviewRepository {
            return ReviewRepository(api)
        }
    }
}
