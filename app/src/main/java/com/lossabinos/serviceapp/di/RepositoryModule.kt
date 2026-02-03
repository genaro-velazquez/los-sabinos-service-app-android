package com.lossabinos.serviceapp.di

import android.content.SharedPreferences
import com.lossabinos.data.datasource.local.ChecklistLocalDataSource
import com.lossabinos.data.dto.repositories.retrofit.authentication.AuthenticationRetrofitRepository
import com.lossabinos.data.dto.utilities.HeadersMaker
import com.lossabinos.data.datasource.local.database.dao.ActivityEvidenceDao
import com.lossabinos.data.datasource.local.database.dao.ActivityProgressDao
import com.lossabinos.data.datasource.local.database.dao.InitialDataDao
import com.lossabinos.data.datasource.local.database.dao.ObservationResponseDao
import com.lossabinos.data.datasource.local.database.dao.ServiceFieldValueDao
import com.lossabinos.data.repositories.ChecklistRepositoryImpl
import com.lossabinos.data.repositories.LocalDataRepositoryImp
import com.lossabinos.data.datasource.local.MechanicsLocalDataSource
import com.lossabinos.data.datasource.local.WorkRequestLocalDataSource
import com.lossabinos.data.datasource.local.WorkRequestPhotoLocalDataSource
import com.lossabinos.data.datasource.local.database.dao.ExtraCostDao
import com.lossabinos.data.datasource.local.database.dao.WorkRequestDao
import com.lossabinos.data.datasource.local.database.dao.WorkRequestPhotoDao
import com.lossabinos.data.datasource.remoto.AuthenticationRemoteDataSource
import com.lossabinos.data.datasource.remoto.ChecklistRemoteDataSource
import com.lossabinos.data.datasource.remoto.MechanicsRemoteDataSource
import com.lossabinos.data.datasource.remoto.NotificationRemoteDataSource
import com.lossabinos.data.datasource.remoto.WorkRequestPhotoRemoteDataSource
import com.lossabinos.data.datasource.remoto.WorkRequestRemoteDataSource
import com.lossabinos.data.mappers.ChecklistProgressRequestMapper
import com.lossabinos.data.mappers.WorkRequestIssueApiMapper
//import com.lossabinos.data.repositories.local.ChecklistRepository
import com.lossabinos.data.repositories.UserSharedPreferencesRepositoryImpl
import com.lossabinos.data.repositories.MechanicsRetrofitRepository
import com.lossabinos.data.repositories.NotificationRetrofitRepository
import com.lossabinos.data.repositories.SystemClock
import com.lossabinos.data.repositories.WebSocketRepositoryImpl
import com.lossabinos.data.repositories.WorkRequestPhotoRepositoryImpl
import com.lossabinos.data.repositories.WorkRequestRepositoryImp
import com.lossabinos.domain.repositories.AuthenticationRepository
import com.lossabinos.domain.repositories.ChecklistRepository
import com.lossabinos.domain.repositories.ClockRepository
import com.lossabinos.domain.repositories.LocalDataRepository
import com.lossabinos.domain.repositories.MechanicsRepository
import com.lossabinos.domain.repositories.NotificationRepository
import com.lossabinos.domain.repositories.UserPreferencesRepository
import com.lossabinos.domain.repositories.WebSocketRepository
import com.lossabinos.domain.repositories.WorkRequestPhotoRepository
import com.lossabinos.domain.repositories.WorkRequestRepository
import com.lossabinos.domain.usecases.authentication.RefreshSessionUseCase
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
        remoteDataSource: MechanicsRemoteDataSource,
        localDataSource: MechanicsLocalDataSource
    ): MechanicsRepository {
        return MechanicsRetrofitRepository(
            remoteDataSource = remoteDataSource,
            localDataSource = localDataSource
        )
    }

    /*
    // ============== CHECKLIST PROGRESS REPOSITORY ==============
    // Administra: activities completadas, evidencias (fotos/videos),
    // respuestas de observations, valores de campos y progreso general del servicio
    @Singleton
    @Provides
    fun provideChecklistRepository(
        activityProgressDao: ActivityProgressDao,
        activityEvidenceDao: ActivityEvidenceDao,
        observationResponseDao: ObservationResponseDao,
        serviceFieldValueDao: ServiceFieldValueDao
    ): ChecklistRepository {
        return ChecklistRepository(
            activityProgressDao,
            activityEvidenceDao,
            observationResponseDao,
            serviceFieldValueDao
        )
    }
*/
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
        checklistLocalDataSource: ChecklistLocalDataSource,
        clock: ClockRepository
    ): ChecklistRepository {
        return ChecklistRepositoryImpl(
            activityProgressDao = activityProgressDao,
            activityEvidenceDao = activityEvidenceDao,
            observationResponseDao = observationResponseDao,
            serviceFieldValueDao = serviceFieldValueDao,
            initialDataDao = initialDataDao,
            checklistProgressRequestMapper = checklistProgressRequestMapper,
            checklistRemoteDataSource = checklistRemoteDataSource,
            checklistLocalDataSource = checklistLocalDataSource,
            clock = clock
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
        serviceFieldValueDao: ServiceFieldValueDao,
        extraCostDao: ExtraCostDao,
        workRequestDao: WorkRequestDao,
        workRequestPhotoDao: WorkRequestPhotoDao
    ): LocalDataRepository {
        return LocalDataRepositoryImp(
            initialDataDao = initialDataDao,
            activityProgressDao = activityProgressDao,
            activityEvidenceDao = activityEvidenceDao,
            observationResponseDao = observationResponseDao,
            serviceFieldValueDao = serviceFieldValueDao,
            extraCostDao = extraCostDao,
            workRequestDao = workRequestDao,
            workRequestPhotoDao = workRequestPhotoDao
        )
    }

    // ============== NOTIFICATIONS REPOSITORY ==============
    @Singleton
    @Provides
    fun provideNotificationReposiory(
        remoteDataSource: NotificationRemoteDataSource
    ): NotificationRepository {
        return NotificationRetrofitRepository(
            remoteDataSource = remoteDataSource
        )
    }

    // ============== WebSocket REPOSITORY ==============
    @Singleton
    @Provides
    fun provideWebSocketRepository(
        refreshSessionUseCase: RefreshSessionUseCase
    ) : WebSocketRepository {
        return WebSocketRepositoryImpl(
            refreshSessionUseCase = refreshSessionUseCase
        )
    }

    // ============== Clock REPOSITORY ==============
    @Singleton
    @Provides
    fun providesClockRepository(
    ) : ClockRepository {
        return SystemClock()
    }

    // ============== Work Request Repository ==============
    @Singleton
    @Provides
    fun provideWorkRequestRepository(
        workRequestLocalDataSource: WorkRequestLocalDataSource,
        workRequestRemoteRepository: WorkRequestRemoteDataSource,
        apiMapper: WorkRequestIssueApiMapper
    ): WorkRequestRepository {
        return WorkRequestRepositoryImp(
            workRequestLocalDataSource = workRequestLocalDataSource,
            workRequestRemoteDataSource = workRequestRemoteRepository,
            apiMapper = apiMapper
        )
    }

    // ============== Work Request Photo Repository ==============
    @Singleton
    @Provides
    fun provideWorkRequestPhotoRepository(
        workRequestPhotoLocalDataSource: WorkRequestPhotoLocalDataSource,
        workRequestPhotoRemoteDataSource: WorkRequestPhotoRemoteDataSource
    ) : WorkRequestPhotoRepository{
        return WorkRequestPhotoRepositoryImpl(
            workRequestPhotoLocalDataSource = workRequestPhotoLocalDataSource,
            workRequestPhotoRemoteDataSource = workRequestPhotoRemoteDataSource
        )
    }
}