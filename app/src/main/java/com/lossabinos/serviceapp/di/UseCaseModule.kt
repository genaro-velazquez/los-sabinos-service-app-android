package com.lossabinos.serviceapp.di

import com.lossabinos.domain.repositories.AuthenticationRepository
import com.lossabinos.domain.repositories.UserPreferencesRepository
import com.lossabinos.domain.usecases.authentication.EmailPasswordLoginUseCase
import com.lossabinos.domain.usecases.preferences.GerUserPreferencesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    // ============== AUTHENTICATION USE CASES ==============

    @Singleton
    @Provides
    fun provideEmailPasswordLoginUseCase(
        authenticationRepository: AuthenticationRepository,
        userPreferencesRepository: UserPreferencesRepository
    ): EmailPasswordLoginUseCase {
        return EmailPasswordLoginUseCase(
            authenticationRepository = authenticationRepository,
            userPreferencesRepository = userPreferencesRepository
        )
    }

    // ============== PREFERENCES USE CASES ==============

    @Singleton
    @Provides
    fun provideGetUserPreferencesUseCase(
        userPreferencesRepository: UserPreferencesRepository
    ): GerUserPreferencesUseCase {
        return GerUserPreferencesUseCase(userPreferencesRepository)
    }

}
