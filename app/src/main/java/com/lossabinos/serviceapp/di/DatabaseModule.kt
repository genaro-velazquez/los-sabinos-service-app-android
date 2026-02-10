package com.lossabinos.serviceapp.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.lossabinos.data.datasource.local.database.AppDatabase
import com.lossabinos.data.datasource.local.database.dao.ActivityEvidenceDao
import com.lossabinos.data.datasource.local.database.dao.ActivityProgressDao
import com.lossabinos.data.datasource.local.database.dao.ExtraCostDao
import com.lossabinos.data.datasource.local.database.dao.InitialDataDao
import com.lossabinos.data.datasource.local.database.dao.ObservationResponseDao
import com.lossabinos.data.datasource.local.database.dao.ServiceFieldValueDao
import com.lossabinos.data.datasource.local.database.dao.ServiceStartDao
import com.lossabinos.data.datasource.local.database.dao.WorkRequestDao
import com.lossabinos.data.datasource.local.database.dao.WorkRequestPhotoDao
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
        )
            .addCallback(object : RoomDatabase.Callback() {
                override fun onOpen(db: SupportSQLiteDatabase) {
                    super.onOpen(db)
                    db.execSQL("PRAGMA foreign_keys=ON;")
                }
            })
            .build()
    }

    @Singleton
    @Provides
    fun provideInitialDataDao(
        database: AppDatabase
    ): InitialDataDao {
        return database.initialDataDao()
    }

    // ✨ NUEVOS PROVIDERS (sin @ColumnInfo)
    @Singleton
    @Provides
    fun provideActivityProgressDao(database: AppDatabase): ActivityProgressDao {
        return database.activityProgressDao()
    }

    @Singleton
    @Provides
    fun provideActivityEvidenceDao(database: AppDatabase): ActivityEvidenceDao {
        return database.activityEvidenceDao()
    }

    @Singleton
    @Provides
    fun provideObservationResponseDao(database: AppDatabase): ObservationResponseDao {
        return database.observationResponseDao()
    }

    @Singleton
    @Provides
    fun provideServiceFieldValueDao(database: AppDatabase): ServiceFieldValueDao {
        return database.serviceFieldValueDao()
    }

    @Provides
    @Singleton
    fun provideExtraCostDao(database: AppDatabase): ExtraCostDao {
        return database.extraCostDao()
    }

    @Provides
    @Singleton
    fun provideServiceStartDao(database: AppDatabase): ServiceStartDao {
        return database.serviceStartDao()
    }

    @Provides
    @Singleton
    fun provideWorkRequestDao(database: AppDatabase): WorkRequestDao {
        return database.workRequestDao()
    }

    @Provides
    @Singleton
    fun providesWorkRequestPhotoDao(database: AppDatabase): WorkRequestPhotoDao{
        return database.workRequestPhotoDao()
    }
}
