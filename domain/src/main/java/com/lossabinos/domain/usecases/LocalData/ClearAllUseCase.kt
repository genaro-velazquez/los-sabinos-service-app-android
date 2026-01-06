package com.lossabinos.domain.usecases.LocalData

import com.lossabinos.domain.repositories.LocalDataRepository

class ClearAllUseCase(
    private val localDataRepository: LocalDataRepository
) {

    suspend operator fun invoke(){
        localDataRepository.clearAll()
    }

}