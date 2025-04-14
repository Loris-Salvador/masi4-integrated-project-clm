package be.hepl.clm.di

import android.content.Context
import be.hepl.clm.data.auth.RetrofitAuthApi
import be.hepl.clm.data.token.TokenDataStoreRepository
import be.hepl.clm.data.token.TokenRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object TokenModule {

    @Provides
    fun provideTokenRepository(@ApplicationContext context: Context): TokenRepository {
        return TokenDataStoreRepository(context)
    }
}