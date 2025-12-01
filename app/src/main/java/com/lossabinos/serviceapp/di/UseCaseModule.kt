package com.lossabinos.serviceapp.di

import com.lossabinos.domain.repositories.AuthenticationRepository
import com.lossabinos.domain.repositories.MechanicsRepository
import com.lossabinos.domain.repositories.UserPreferencesRepository
import com.lossabinos.domain.usecases.authentication.EmailPasswordLoginUseCase
import com.lossabinos.domain.usecases.mechanics.GetMechanicsServicesUseCase
import com.lossabinos.domain.usecases.preferences.GetUserPreferencesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    // ============== PREFERENCES USE CASES ==============

    @Singleton
    @Provides
    fun provideGetUserPreferencesUseCase(
        userPreferencesRepository: UserPreferencesRepository
    ): GetUserPreferencesUseCase {
        return GetUserPreferencesUseCase(userPreferencesRepository)
    }

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

    // ============== MECHANICS USE CASES ==============

    @Singleton
    @Provides
    fun provideGetMechanicsServicesUseCase(
        mechanicsRepository: MechanicsRepository
    ) : GetMechanicsServicesUseCase{
        return GetMechanicsServicesUseCase(mechanicsRepository = mechanicsRepository)
    }

}
