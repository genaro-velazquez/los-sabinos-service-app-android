package com.lossabinos.serviceapp.di

import android.content.Context
import androidx.room.Room
import com.lossabinos.data.local.database.AppDatabase
import com.lossabinos.data.local.database.dao.InitialDataDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "los_sabinos.db"
            ).fallbackToDestructiveMigration(false)
        .build()
    }

    @Singleton
    @Provides
    fun provideInitialDataDao(
        database: AppDatabase
    ): InitialDataDao {
        return database.initialDataDao()
    }
}
