package be.hepl.clm.di

import be.hepl.clm.data.purchase.PurchaseApiService
import be.hepl.clm.data.purchase.PurchaseRepository
import be.hepl.clm.data.purchase.PurchaseRepositoryImpl
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
abstract class PurchaseModule {

    @Binds
    @Singleton
    abstract fun bindPurchaseRepository(
        purchaseRepositoryImpl: PurchaseRepositoryImpl
    ): PurchaseRepository

    companion object {
        @Provides
        @Singleton
        fun providePurchaseApiService(@Named("ArticleAPI") retrofit: Retrofit): PurchaseApiService {
            return retrofit.create(PurchaseApiService::class.java)
        }
    }
}