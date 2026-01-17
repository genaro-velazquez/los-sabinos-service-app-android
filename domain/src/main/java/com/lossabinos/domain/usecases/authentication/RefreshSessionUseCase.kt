package com.lossabinos.domain.usecases.authentication

import com.lossabinos.domain.repositories.AuthenticationRepository
import com.lossabinos.domain.repositories.UserPreferencesRepository
import com.lossabinos.domain.valueobjects.Token

class RefreshSessionUseCase(
    private val authenticationRepository: AuthenticationRepository,
    userPreferencesRepository: UserPreferencesRepository
) : SetUserPreferencesOnRefreshToken(userPreferencesRepository = userPreferencesRepository) {

    suspend fun execute(): Token {
        val refreshToken = authenticationRepository.getRefreshToken()
        val token = authenticationRepository.refreshToken(refreshToken)
        setUserPreferencesOnRefreshToken(token = token)
        return token
    }

}