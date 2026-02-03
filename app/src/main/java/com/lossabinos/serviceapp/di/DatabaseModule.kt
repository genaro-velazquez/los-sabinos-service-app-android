package com.lossabinos.serviceapp.di

import android.content.Context
import androidx.room.Room
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

    private val MIGRATION_9_TO_10 = object : Migration(
        startVersion = 9,
        endVersion = 10
    ){
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL(
                """
            ALTER TABLE work_request
            ADD COLUMN conceptCategory TEXT NOT NULL DEFAULT 'OTHER'
            """.trimIndent()
            )
        }
    }

    private val MIGRATION_8_TO_9 = object : Migration(
        startVersion = 8,
        endVersion = 9
    ){
        override fun migrate(db: SupportSQLiteDatabase) {
            // Agregar la nueva columna
            db.execSQL(
                "ALTER TABLE activity_progress ADD COLUMN activityId TEXT NOT NULL DEFAULT ''"
            )

            // Agregar índice para mejor performance
            db.execSQL(
                "CREATE INDEX IF NOT EXISTS index_activity_progress_activityId ON activity_progress(activityId)"
            )
        }
    }

    private val MIGRATION_7_TO_8 = object : Migration(
        startVersion = 7,
        endVersion = 8
    ){
        override fun migrate(db: SupportSQLiteDatabase) {
            // Crear tabla temporal con nueva estructura
            db.execSQL("""
            CREATE TABLE observation_response_new (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                assignedServiceId TEXT NOT NULL,
                sectionIndex INTEGER NOT NULL,
                observationIndex INTEGER NOT NULL,
                observationId TEXT NOT NULL,
                observationDescription TEXT NOT NULL,
                responseType TEXT NOT NULL,
                response TEXT,
                requiresResponse INTEGER NOT NULL DEFAULT 0,
                timestamp TEXT NOT NULL,
                FOREIGN KEY(assignedServiceId) REFERENCES assigned_services(id) ON DELETE CASCADE
            )
        """.trimIndent())

            // Copiar datos de tabla antigua a nueva
            db.execSQL("""
            INSERT INTO observation_response_new (
                id, assignedServiceId, sectionIndex, observationIndex,
                observationId, observationDescription, responseType,
                response, requiresResponse, timestamp
            )
            SELECT 
                id, assignedServiceId, sectionIndex, observationIndex,
                '', observationDescription, 'textarea',
                response, 0, CURRENT_TIMESTAMP
            FROM observation_response
        """.trimIndent())

            // Eliminar tabla antigua
            db.execSQL("DROP TABLE observation_response")

            // Renombrar tabla nueva
            db.execSQL("ALTER TABLE observation_response_new RENAME TO observation_response")
        }
    }

    private val MIGRATION_6_TO_7 = object : Migration(
        startVersion = 6,
        endVersion = 7
    ){
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("""
                    CREATE TABLE IF NOT EXISTS service_progress (
                        assignedServiceId TEXT PRIMARY KEY NOT NULL,
                        completedActivities INTEGER NOT NULL DEFAULT 0,
                        totalActivities INTEGER NOT NULL DEFAULT 0,
                        completedPercentage INTEGER NOT NULL DEFAULT 0,
                        status TEXT NOT NULL,
                        lastUpdatedAt INTEGER NOT NULL DEFAULT 0,
                        syncStatus TEXT NOT NULL DEFAULT 'PENDING'
                    )
                """)
        }
    }

    private val MIGRATION_5_TO_6 = object : Migration(5, 6) {
        override fun migrate(db: SupportSQLiteDatabase) {
            // Agregar 2 columnas nuevas a service_types
            db.execSQL("""
            ALTER TABLE service_types 
            ADD COLUMN code TEXT NOT NULL DEFAULT ''
        """)

            db.execSQL("""
            ALTER TABLE service_types 
            ADD COLUMN category TEXT NOT NULL DEFAULT ''
        """)
        }
    }

    private val MIGRATION_4_TO_5 = object : Migration(4, 5) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("""
            CREATE TABLE IF NOT EXISTS sync_metadata (
                id TEXT PRIMARY KEY NOT NULL,
                serverTimestamp TEXT NOT NULL,
                totalServices INTEGER NOT NULL,
                pendingServices INTEGER NOT NULL,
                inProgressServices INTEGER NOT NULL,
                lastSync TEXT,
                updatedAt TEXT NOT NULL
            )
        """)
        }
    }

    private val MIGRATION_3_TO_4 = object : Migration(3,4){
        override fun migrate(db: SupportSQLiteDatabase) {
            // Agregar 2 columnas nuevas a mechanics
            db.execSQL("""
            ALTER TABLE mechanics 
            ADD COLUMN zoneId TEXT NOT NULL DEFAULT ''
        """)

            db.execSQL("""
            ALTER TABLE mechanics 
            ADD COLUMN zoneName TEXT NOT NULL DEFAULT ''
        """)

        }
    }

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
            .addMigrations(MIGRATION_2_TO_3,
                MIGRATION_3_TO_4,
                MIGRATION_4_TO_5,
                MIGRATION_5_TO_6,
                MIGRATION_6_TO_7,
                MIGRATION_7_TO_8,
                MIGRATION_8_TO_9,
                MIGRATION_9_TO_10)
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