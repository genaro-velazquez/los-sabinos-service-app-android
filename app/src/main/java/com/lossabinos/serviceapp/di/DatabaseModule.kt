package com.lossabinos.serviceapp.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.lossabinos.data.local.database.AppDatabase
import com.lossabinos.data.local.database.dao.ActivityEvidenceDao
import com.lossabinos.data.local.database.dao.ActivityProgressDao
import com.lossabinos.data.local.database.dao.InitialDataDao
import com.lossabinos.data.local.database.dao.ObservationResponseDao
import com.lossabinos.data.local.database.dao.ServiceFieldValueDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    private val MIGRATION_2_TO_3 = object : Migration(2,3){
        override fun migrate(db: SupportSQLiteDatabase) {
            // Tabla: activity_progress (camelCase)
            db.execSQL("""
                CREATE TABLE IF NOT EXISTS activity_progress (
                    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    assignedServiceId TEXT NOT NULL,
                    sectionIndex INTEGER NOT NULL,
                    activityIndex INTEGER NOT NULL,
                    activityDescription TEXT NOT NULL,
                    requiresEvidence INTEGER NOT NULL,
                    completed INTEGER NOT NULL DEFAULT 0,
                    completedAt TEXT,
                    FOREIGN KEY(assignedServiceId) REFERENCES assigned_services(id) ON DELETE CASCADE
                )
            """)

            // Tabla: activity_evidence (camelCase)
            db.execSQL("""
                CREATE TABLE IF NOT EXISTS activity_evidence (
                    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    activityProgressId INTEGER NOT NULL,
                    filePath TEXT NOT NULL,
                    fileType TEXT NOT NULL DEFAULT 'image',
                    timestamp TEXT NOT NULL,
                    FOREIGN KEY(activityProgressId) REFERENCES activity_progress(id) ON DELETE CASCADE
                )
            """)

            // Tabla: observation_response (camelCase)
            db.execSQL("""
                CREATE TABLE IF NOT EXISTS observation_response (
                    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    assignedServiceId TEXT NOT NULL,
                    sectionIndex INTEGER NOT NULL,
                    observationIndex INTEGER NOT NULL,
                    observationDescription TEXT NOT NULL,
                    response TEXT,
                    timestamp TEXT,
                    FOREIGN KEY(assignedServiceId) REFERENCES assigned_services(id) ON DELETE CASCADE
                )
            """)

            // Tabla: service_field_value (camelCase)
            db.execSQL("""
                CREATE TABLE IF NOT EXISTS service_field_value (
                    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    assignedServiceId TEXT NOT NULL,
                    fieldIndex INTEGER NOT NULL,
                    fieldLabel TEXT NOT NULL,
                    fieldType TEXT NOT NULL,
                    required INTEGER NOT NULL,
                    value TEXT,
                    timestamp TEXT,
                    FOREIGN KEY(assignedServiceId) REFERENCES assigned_services(id) ON DELETE CASCADE
                )
            """)

            // ✨ Agregar columnas a assigned_services (camelCase)
            db.execSQL("""
                ALTER TABLE assigned_services 
                ADD COLUMN checklistTemplateJson TEXT
            """)

            db.execSQL("""
                ALTER TABLE assigned_services 
                ADD COLUMN progressPercentage INTEGER DEFAULT 0
            """)

            // ✨ Agregar columnas faltantes a assigned_services
            db.execSQL("""
                ALTER TABLE assigned_services 
                ADD COLUMN workOrderNumber TEXT DEFAULT ''
            """)

            db.execSQL("""
                ALTER TABLE assigned_services 
                ADD COLUMN serviceTypeName TEXT DEFAULT ''
            """)

            db.execSQL("""
                ALTER TABLE assigned_services 
                ADD COLUMN vehicleId TEXT DEFAULT ''
            """)

            db.execSQL("""
                ALTER TABLE assigned_services 
                ADD COLUMN vehicleVin TEXT
            """)

            db.execSQL("""
                ALTER TABLE assigned_services 
                ADD COLUMN vehicleEconomicNumber TEXT
            """)

            db.execSQL("""
                ALTER TABLE assigned_services 
                ADD COLUMN vehicleModelName TEXT
            """)

        }
    }

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
            .addMigrations(MIGRATION_2_TO_3)
            .fallbackToDestructiveMigration(true)
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

}

/*
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
*/