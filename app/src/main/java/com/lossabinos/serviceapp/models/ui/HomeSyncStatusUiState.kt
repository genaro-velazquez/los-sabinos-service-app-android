package com.lossabinos.serviceapp.models.ui

import java.time.Instant

data class HomeSyncStatusUiState(
    val isOnline: Boolean = true,
    val lastSyncAt: Instant? = null // o Long timestamp, lo que ya uses
)
