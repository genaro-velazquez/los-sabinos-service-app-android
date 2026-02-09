package com.lossabinos.serviceapp.di

import android.content.Context
import com.lossabinos.data.datastore.sync.SyncPreferencesDataSource
import com.lossabinos.data.repositories.HomeSyncStatusRepositoryImpl
import com.lossabinos.domain.repositories.HomeSyncStatusRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {
    @Provides
    @Singleton
    fun provideSyncPreferencesDataSource(
        @ApplicationContext context: Context
    ): SyncPreferencesDataSource =
        SyncPreferencesDataSource(context)

    @Provides
    @Singleton
    fun provideHomeSyncStatusRepository(
        dataSource: SyncPreferencesDataSource
    ): HomeSyncStatusRepository =
        HomeSyncStatusRepositoryImpl(dataSource)

}