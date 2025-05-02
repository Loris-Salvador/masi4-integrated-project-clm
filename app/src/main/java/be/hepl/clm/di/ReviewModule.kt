package be.hepl.clm.di

import be.hepl.clm.data.auth.AuthRepository
import be.hepl.clm.data.auth.AuthRepositoryImpl
import be.hepl.clm.data.auth.RetrofitAuthApi
import be.hepl.clm.data.bank.BankApiService
import be.hepl.clm.data.purchase.PurchaseApiService
import be.hepl.clm.data.purchase.PurchaseRepository
import be.hepl.clm.data.purchase.PurchaseRepositoryImpl
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
