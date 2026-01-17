package com.lossabinos.data.datasource.local

import android.content.SharedPreferences
import com.lossabinos.domain.repositories.UserPreferencesRepository
import javax.inject.Inject

class TokenManager @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) {
    fun getAccessToken(): String =
        userPreferencesRepository.getAccessToken()

    fun getRefreshToken(): String =
        userPreferencesRepository.getRefreshToken()

    fun saveTokens(
        accessToken: String,
        refreshToken: String
    ) {
        userPreferencesRepository.setAccessToken(accessToken)
        userPreferencesRepository.setRefreshToken(refreshToken)
    }

    fun clearTokens() {
        userPreferencesRepository.clear()
    }

    fun hasAccessToken(): Boolean =
        userPreferencesRepository.getAccessToken().isNotBlank()

    fun hasRefreshToken(): Boolean =
        userPreferencesRepository.getRefreshToken().isNotBlank()

}