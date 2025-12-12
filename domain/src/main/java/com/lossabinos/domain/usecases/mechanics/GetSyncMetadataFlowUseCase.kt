package com.lossabinos.domain.usecases.mechanics

import com.lossabinos.domain.repositories.MechanicsRepository
import com.lossabinos.domain.valueobjects.SyncMetadata
import kotlinx.coroutines.flow.Flow

class GetSyncMetadataFlowUseCase(
    private val repository: MechanicsRepository
) {
    operator fun invoke(): Flow<SyncMetadata?> {
        return repository.getSyncMetadataFlow()
    }
}
