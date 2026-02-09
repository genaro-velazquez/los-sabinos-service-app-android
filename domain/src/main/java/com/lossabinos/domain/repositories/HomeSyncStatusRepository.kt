package com.lossabinos.domain.repositories

import kotlinx.coroutines.flow.Flow
import java.time.Instant

interface HomeSyncStatusRepository {
    val lastServicesRefreshAt: Flow<Instant?>
    suspend fun updateLastServicesRefreshAt(instant: Instant)
}
