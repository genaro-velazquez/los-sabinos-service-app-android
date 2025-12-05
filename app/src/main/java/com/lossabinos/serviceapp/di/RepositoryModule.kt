package com.lossabinos.serviceapp.di

import android.content.SharedPreferences
import com.lossabinos.data.dto.repositories.retrofit.authentication.AuthenticationRetrofitRepository
import com.lossabinos.data.dto.utilities.HeadersMaker
import com.lossabinos.data.local.database.dao.InitialDataDao
import com.lossabinos.data.repositories.local.UserSharedPreferencesRepositoryImpl
import com.lossabinos.data.repositories.retrofit.authentication.AuthenticationServices
import com.lossabinos.data.repositories.retrofit.mechanics.MechanicsRetrofitRepository
import com.lossabinos.data.repositories.retrofit.mechanics.MechanicsServices
import com.lossabinos.domain.repositories.AuthenticationRepository
import com.lossabinos.domain.repositories.MechanicsRepository
import com.lossabinos.domain.repositories.UserPreferencesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    // ============== USER PREFERENCES REPOSITORY ==============
    @Singleton
    @Provides
    fun provideUserPreferencesRepository(
        sharedPreferences: SharedPreferences
    ): UserPreferencesRepository {
        return UserSharedPreferencesRepositoryImpl(sharedPreferences)
    }

    // ============== AUTHENTICATION REPOSITORY ==============
    @Singleton
    @Provides
    fun provideHeadersMaker(
        userPreferencesRepository: UserPreferencesRepository
    ): HeadersMaker {
        return HeadersMaker(
            userPreferencesRepository = userPreferencesRepository,
            language = "es"
        )
    }
    @Singleton
    @Provides
    fun provideAuthenticationRepository(
        authenticationServices: AuthenticationServices,
        headersMaker: HeadersMaker
    ): AuthenticationRepository {
        return AuthenticationRetrofitRepository(
            authenticationServices = authenticationServices,
            headersMaker = headersMaker
        )
    }

    // ============== MECHANICS REPOSITORY ==============
    @Singleton
    @Provides
    fun provideMechanicsRepository(
        mechanicsServices: MechanicsServices,
        headersMaker: HeadersMaker,
        initialDataDao: InitialDataDao
    ): MechanicsRepository{
        return MechanicsRetrofitRepository(
            assignedServices = mechanicsServices,
            headersMaker = headersMaker,
            initialDataDao = initialDataDao
        )
    }

}