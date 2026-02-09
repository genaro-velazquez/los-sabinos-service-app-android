package com.lossabinos.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.lossabinos.data.datastore.sync.SyncPreferences

private const val SYNC_PREFERENCES_FILE_NAME = "sync_preferences.pb"

val Context.syncPreferencesDataStore: DataStore<SyncPreferences> by dataStore(
    fileName = SYNC_PREFERENCES_FILE_NAME,
    serializer = SyncPreferencesSerializer
)
