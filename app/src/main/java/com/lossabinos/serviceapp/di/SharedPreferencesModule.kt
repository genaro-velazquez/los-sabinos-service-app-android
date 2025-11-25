package com.lossabinos.serviceapp.di

import android.content.Context
import android.content.SharedPreferences
import com.lossabinos.data.repositories.local.UserSharedPreferencesRepositoryImpl
import com.lossabinos.domain.repositories.UserPreferencesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SharedPreferencesModule {

    @Singleton
    @Provides
    fun provideSharedPreferences(
        @ApplicationContext context: Context
    ): SharedPreferences {
        return context.getSharedPreferences("los_sabinos_prefs", Context.MODE_PRIVATE)
    }

}
