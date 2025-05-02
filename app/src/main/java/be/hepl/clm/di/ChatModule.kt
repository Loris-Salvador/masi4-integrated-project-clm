package be.hepl.clm.di

import be.hepl.clm.data.chat.ChatApiService
import be.hepl.clm.data.chat.ChatRepository
import be.hepl.clm.data.chat.ChatRepositoryImpl
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
abstract class ChatRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindChatRepository(impl: ChatRepositoryImpl): ChatRepository
}

@Module
@InstallIn(SingletonComponent::class)
object ChatApiModule {
    @Provides
    @Singleton
    fun provideChatApiService(@Named("Chat") retrofit: Retrofit): ChatApiService {
        return retrofit.create(ChatApiService::class.java)
    }
}