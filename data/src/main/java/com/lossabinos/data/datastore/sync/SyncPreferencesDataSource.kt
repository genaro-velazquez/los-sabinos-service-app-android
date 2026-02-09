package com.lossabinos.data.datastore.sync

import android.content.Context
import com.lossabinos.data.datastore.syncPreferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Instant

class SyncPreferencesDataSource(
    private val context: Context
) {

    val lastServicesSyncAt: Flow<Instant?> =
        context.syncPreferencesDataStore.data
            .map { prefs ->
                if (prefs.lastServicesSyncAt == 0L) null
                else Instant.ofEpochMilli(prefs.lastServicesSyncAt)
            }

    suspend fun updateLastServicesSyncAt(instant: Instant) {
        context.syncPreferencesDataStore.updateData { current ->
            current.toBuilder()
                .setLastServicesSyncAt(instant.toEpochMilli())
                .build()
        }
    }
}