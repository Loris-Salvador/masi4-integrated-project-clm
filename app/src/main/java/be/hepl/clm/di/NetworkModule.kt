package be.hepl.clm.di

import be.hepl.clm.data.auth.RetrofitAuthApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    @Named("AuthAPI")
    fun provideRetrofitAuth(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://95.182.245.78:59001/api/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    @Named("ArticleAPI")
    fun provideRetrofitArticle(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://95.182.245.78:59006/api/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}