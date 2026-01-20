package com.lossabinos.serviceapp.di

import android.content.Context
import android.content.SharedPreferences
import androidx.work.WorkManager
import com.lossabinos.data.datasource.local.ChecklistLocalDataSource
import com.lossabinos.data.datasource.local.LocationWebSocketDataSource
import com.lossabinos.data.datasource.local.OfflineLocationQueue
import com.lossabinos.data.datasource.local.TokenManager
import com.lossabinos.data.datasource.local.WebSocketClient
import com.lossabinos.data.dto.repositories.retrofit.authentication.AuthenticationRetrofitRepository
import com.lossabinos.data.dto.utilities.HeadersMaker
import com.lossabinos.data.datasource.local.database.dao.ActivityEvidenceDao
import com.lossabinos.data.datasource.local.database.dao.ActivityProgressDao
import com.lossabinos.data.datasource.local.database.dao.InitialDataDao
import com.lossabinos.data.datasource.local.database.dao.LocationDao
import com.lossabinos.data.datasource.local.database.dao.ObservationResponseDao
import com.lossabinos.data.datasource.local.database.dao.ServiceFieldValueDao
import com.lossabinos.data.repositories.ChecklistRepositoryImpl
import com.lossabinos.data.repositories.LocalDataRepositoryImp
import com.lossabinos.data.datasource.local.MechanicsLocalDataSource
import com.lossabinos.data.datasource.remoto.AuthenticationRemoteDataSource
import com.lossabinos.data.datasource.remoto.ChecklistRemoteDataSource
import com.lossabinos.data.datasource.remoto.MechanicsRemoteDataSource
import com.lossabinos.data.mappers.ChecklistProgressRequestMapper
import com.lossabinos.data.repositories.LocationSocketRepositoryImpl
import com.lossabinos.data.repositories.UserSharedPreferencesRepositoryImpl
import com.lossabinos.data.repositories.MechanicsRetrofitRepository
import com.lossabinos.data.utilities.NetworkStateMonitor
import com.lossabinos.domain.repositories.AuthenticationRepository
import com.lossabinos.domain.repositories.ChecklistRepository
import com.lossabinos.domain.repositories.LocalDataRepository
import com.lossabinos.domain.repositories.LocationSocketRepository
import com.lossabinos.domain.repositories.MechanicsRepository
import com.lossabinos.domain.repositories.UserPreferencesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import javax.inject.Provider
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
        remoteDataSource: AuthenticationRemoteDataSource,
        userPreferencesRepository: UserPreferencesRepository
    ): AuthenticationRepository {
        return AuthenticationRetrofitRepository(
            remoteDataSource = remoteDataSource,
            userPreferencesRepository = userPreferencesRepository
        )
    }

    // ============== MECHANICS REPOSITORY ==============
    @Singleton
    @Provides
    fun provideMechanicsRepositoryImp(
        remoteDataSource : MechanicsRemoteDataSource,
        localDataSource : MechanicsLocalDataSource
    ): MechanicsRepository{
        return MechanicsRetrofitRepository(
            remoteDataSource = remoteDataSource,
            localDataSource = localDataSource
        )
    }

    @Singleton
    @Provides
    fun provideChecklistRepository(
        activityProgressDao: ActivityProgressDao,
        activityEvidenceDao: ActivityEvidenceDao,
        observationResponseDao: ObservationResponseDao,
        serviceFieldValueDao: ServiceFieldValueDao,
        initialDataDao: InitialDataDao,
        checklistProgressRequestMapper: ChecklistProgressRequestMapper,
        checklistRemoteDataSource: ChecklistRemoteDataSource,
        checklistLocalDataSource: ChecklistLocalDataSource
    ) : ChecklistRepository {
        return ChecklistRepositoryImpl(
            activityProgressDao = activityProgressDao,
            activityEvidenceDao = activityEvidenceDao,
            observationResponseDao = observationResponseDao,
            serviceFieldValueDao =  serviceFieldValueDao,
            initialDataDao = initialDataDao,
            checklistProgressRequestMapper = checklistProgressRequestMapper,
            checklistRemoteDataSource = checklistRemoteDataSource,
            checklistLocalDataSource = checklistLocalDataSource
        )
    }

    // ============== LOCAL DATA REPOSITORY ==============
    @Singleton
    @Provides
    fun provideLocalDataRepository(
        initialDataDao: InitialDataDao,
        activityProgressDao: ActivityProgressDao,
        activityEvidenceDao: ActivityEvidenceDao,
        observationResponseDao: ObservationResponseDao,
        serviceFieldValueDao: ServiceFieldValueDao
    ) : LocalDataRepository{
        return LocalDataRepositoryImp(
            initialDataDao = initialDataDao,
            activityProgressDao = activityProgressDao,
            activityEvidenceDao = activityEvidenceDao,
            observationResponseDao = observationResponseDao,
            serviceFieldValueDao = serviceFieldValueDao
        )
    }

    // ============== LOCATION SOCKET REPOSITORY ==============
    @Provides
    @Singleton
    fun provideLocationWebSocketDataSource(
        webSocketClient: WebSocketClient
    ): LocationWebSocketDataSource {
        return LocationWebSocketDataSource(webSocketClient)
    }

    @Provides
    @Singleton
    fun provideLocationSocketRepository(
        dataSource: LocationWebSocketDataSource
    ): LocationSocketRepository {
        return LocationSocketRepositoryImpl(dataSource)
    }

    @Provides
    @Singleton
    fun provideWorkManager(@ApplicationContext context: Context): WorkManager {
        return WorkManager.getInstance(context)
    }

    @Provides
    @Singleton
    fun provideWebSocketClient(
        tokenManager: TokenManager,
        userPreferencesRepository: UserPreferencesRepository,
        workManager: WorkManager,
        locationDao: LocationDao,
        offlineLocationQueueProvider: Provider<OfflineLocationQueue>,
        networkStateMonitor: NetworkStateMonitor,
        scope: CoroutineScope
    ): WebSocketClient {
        return WebSocketClient(
            tokenManager = tokenManager,
            userPreferencesRepository = userPreferencesRepository,
            workManager = workManager,
            locationDao = locationDao,
            offlineLocationQueueProvider = offlineLocationQueueProvider,
            networkStateMonitor = networkStateMonitor,
            scope = scope
        )
    }
}
