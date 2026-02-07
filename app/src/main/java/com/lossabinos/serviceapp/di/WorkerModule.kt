package com.lossabinos.serviceapp.di

import com.lossabinos.data.repositories.WorkRequestSyncSchedulerImpl
import com.lossabinos.domain.repositories.WorkRequestSyncScheduler
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import dagger.Provides


@Module
@InstallIn(SingletonComponent::class)
object WorkerModule {

    @Singleton
    @Provides
    fun provideWorkRequestSyncScheduler(
        impl: WorkRequestSyncSchedulerImpl
    ): WorkRequestSyncScheduler = impl
}
