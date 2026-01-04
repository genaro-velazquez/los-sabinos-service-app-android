package com.lossabinos.serviceapp.di

import com.lossabinos.domain.repositories.AuthenticationRepository
import com.lossabinos.domain.repositories.ChecklistRepository
import com.lossabinos.domain.repositories.MechanicsRepository
import com.lossabinos.domain.repositories.UserPreferencesRepository
import com.lossabinos.domain.usecases.authentication.EmailPasswordLoginUseCase
import com.lossabinos.domain.usecases.checklist.CompleteActivityUseCase
import com.lossabinos.domain.usecases.checklist.DeleteActivityEvidenceByIdUseCase
import com.lossabinos.domain.usecases.checklist.GetActivitiesProgressForSectionUseCase
import com.lossabinos.domain.usecases.checklist.GetEvidenceForActivityUseCase
import com.lossabinos.domain.usecases.checklist.GetObservationResponsesForSectionUseCase
import com.lossabinos.domain.usecases.checklist.GetServiceFieldValuesUseCase
import com.lossabinos.domain.usecases.checklist.GetTotalCompletedActivitiesUseCase
import com.lossabinos.domain.usecases.checklist.SaveActivityEvidenceUseCase
import com.lossabinos.domain.usecases.checklist.SaveObservationResponseUseCase
import com.lossabinos.domain.usecases.checklist.SaveServiceFieldValueUseCase
import com.lossabinos.domain.usecases.checklist.SaveServiceFieldValuesUseCase
import com.lossabinos.domain.usecases.mechanics.GetAssignedServicesFlowUseCase
import com.lossabinos.domain.usecases.mechanics.GetDetailedServiceUseCase
import com.lossabinos.domain.usecases.mechanics.GetLocalInitialDataUseCase
import com.lossabinos.domain.usecases.mechanics.GetMechanicFlowUseCase
import com.lossabinos.domain.usecases.mechanics.GetMechanicsServicesUseCase
import com.lossabinos.domain.usecases.mechanics.GetServiceTypesFlowUseCase
import com.lossabinos.domain.usecases.mechanics.GetSyncInitialDataUseCase
import com.lossabinos.domain.usecases.mechanics.GetSyncMetadataFlowUseCase
import com.lossabinos.domain.usecases.mechanics.UpdateServiceProgressUseCase
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

    @Singleton
    @Provides
    fun provideGetDetailedService(
        mechanicsRepository: MechanicsRepository
    ) : GetDetailedServiceUseCase{
        return GetDetailedServiceUseCase(mechanicsRepository = mechanicsRepository)
    }

    @Singleton
    @Provides
    fun ProvideGetSyncInitialDataUseCase(
        mechanicsRepository: MechanicsRepository
    ) : GetSyncInitialDataUseCase{
        return GetSyncInitialDataUseCase(mechanicsRepository = mechanicsRepository)
    }

    @Singleton
    @Provides
    fun ProvideGetLocalInitialDataUseCase(
        mechanicsRepository: MechanicsRepository
    ) : GetLocalInitialDataUseCase{
        return GetLocalInitialDataUseCase(mechanicsRepository = mechanicsRepository)
    }

    // Use case database local //
    @Singleton
    @Provides
    fun provideGetMechanicFlowUseCase(
        repository: MechanicsRepository
    ): GetMechanicFlowUseCase {
        return GetMechanicFlowUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideGetAssignedServicesFlowUseCase(
        repository: MechanicsRepository
    ): GetAssignedServicesFlowUseCase {
        return GetAssignedServicesFlowUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideGetServiceTypesFlowUseCase(
        repository: MechanicsRepository
    ): GetServiceTypesFlowUseCase {
        return GetServiceTypesFlowUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideGetSyncMetadataFlowUseCase(
        repository: MechanicsRepository
    ): GetSyncMetadataFlowUseCase {
        return GetSyncMetadataFlowUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideUpdateServiceProgressUseCase(
        repository: MechanicsRepository
    ) : UpdateServiceProgressUseCase{
        return UpdateServiceProgressUseCase(
            repository = repository
        )
    }

    //******************************
    // UseCases ChecklistRepository
    //******************************
    @Singleton
    @Provides
    fun provideCompleteActivityUseCase(
        repository: ChecklistRepository
    ) : CompleteActivityUseCase {
        return CompleteActivityUseCase(
            checklistRepository = repository
        )
    }

    @Singleton
    @Provides
    fun provideGetActivitiesProgressForUseCase(
        repository: ChecklistRepository
    ) : GetActivitiesProgressForSectionUseCase{
        return GetActivitiesProgressForSectionUseCase(
            checklistRepository = repository
        )
    }

    @Singleton
    @Provides
    fun provideGetEvidenceForActivityUseCase(
        repository: ChecklistRepository
    ) : GetEvidenceForActivityUseCase {
        return GetEvidenceForActivityUseCase(
            checklistRepository = repository
        )
    }

    @Singleton
    @Provides
    fun provideGetObservationResponsesForSectionUseCase(
        repository: ChecklistRepository
    ) : GetObservationResponsesForSectionUseCase{
        return GetObservationResponsesForSectionUseCase(
            checklistRepository = repository
        )
    }

    @Singleton
    @Provides
    fun provideGetTotalCompletedActivitiesUseCase(
        repository: ChecklistRepository
    ) : GetTotalCompletedActivitiesUseCase {
        return GetTotalCompletedActivitiesUseCase(
            checklistRepository = repository
        )
    }

    @Singleton
    @Provides
    fun provideSaveActivityEvidenceUseCase(
        repository: ChecklistRepository
    ) : SaveActivityEvidenceUseCase{
        return SaveActivityEvidenceUseCase(
            checklistRepository = repository
        )
    }

    @Singleton
    @Provides
    fun provideSaveObservationResponseUseCase(
        repository: ChecklistRepository
    ) : SaveObservationResponseUseCase{
        return SaveObservationResponseUseCase(
            checklistRepository = repository
        )
    }

    @Singleton
    @Provides
    fun provideSaveServiceFieldValueUseCase(
        repository: ChecklistRepository
    ) : SaveServiceFieldValueUseCase{
        return SaveServiceFieldValueUseCase(
            checklistRepository = repository
        )
    }

    @Singleton
    @Provides
    fun provideSaveServiceFieldValuesUseCase(
        repository: ChecklistRepository
    ) : SaveServiceFieldValuesUseCase{
        return SaveServiceFieldValuesUseCase(
            checklistRepository = repository
        )
    }

    @Singleton
    @Provides
    fun provideGetServiceFieldValuesUseCase(
        repository: ChecklistRepository
    ) : GetServiceFieldValuesUseCase{
        return GetServiceFieldValuesUseCase(
            checklistRepository = repository
        )
    }

    @Singleton
    @Provides
    fun provideDeleteActivityEvidenceByIdUseCase(
        repository: ChecklistRepository
    ) : DeleteActivityEvidenceByIdUseCase{
        return DeleteActivityEvidenceByIdUseCase(
            checklistRepository = repository
        )
    }
}
