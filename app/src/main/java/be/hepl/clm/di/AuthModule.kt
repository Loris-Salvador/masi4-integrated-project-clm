package be.hepl.clm.di

import be.hepl.clm.data.auth.AuthRepositoryImpl
import be.hepl.clm.data.auth.RetrofitAuthApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    fun provideAuthRepository(api: RetrofitAuthApi): AuthRepositoryImpl {
        return AuthRepositoryImpl(api)
    }
}