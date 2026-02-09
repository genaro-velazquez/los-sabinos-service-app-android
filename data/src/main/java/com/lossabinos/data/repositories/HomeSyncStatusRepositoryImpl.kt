package com.lossabinos.data.repositories

import com.lossabinos.data.datastore.sync.SyncPreferencesDataSource
import com.lossabinos.domain.repositories.HomeSyncStatusRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import java.time.Instant

class HomeSyncStatusRepositoryImpl @Inject constructor(
    private val syncPreferencesDataSource: SyncPreferencesDataSource,
) : HomeSyncStatusRepository {

    override val lastServicesRefreshAt: Flow<Instant?> =
        syncPreferencesDataSource.lastServicesSyncAt

    override suspend fun updateLastServicesRefreshAt(instant: Instant) {
        syncPreferencesDataSource.updateLastServicesSyncAt(instant = instant)
    }
}