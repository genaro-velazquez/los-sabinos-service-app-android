package com.lossabinos.domain.usecases.authentication

import com.lossabinos.domain.repositories.UserPreferencesRepository
import com.lossabinos.domain.valueobjects.Token

abstract class SetUserPreferencesOnRefreshToken(
    private val userPreferencesRepository: UserPreferencesRepository
) {

    protected fun setUserPreferencesOnRefreshToken(token: Token){
        userPreferencesRepository.setAccessToken(token = token.accessToken)
        userPreferencesRepository.setRefreshToken(refreshToken = token.refreshToken)
    }

}