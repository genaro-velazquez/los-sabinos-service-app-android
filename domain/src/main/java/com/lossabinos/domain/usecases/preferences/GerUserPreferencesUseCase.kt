package com.lossabinos.domain.usecases.preferences

import com.lossabinos.domain.repositories.UserPreferencesRepository

class GerUserPreferencesUseCase(private val userPreferencesRepository: UserPreferencesRepository) {
    fun getPrimaryColor() = userPreferencesRepository.getPrimaryColor()
    fun getSecondaryColor() = userPreferencesRepository.getSecondaryColor()
    fun getNameApp() = userPreferencesRepository.getNameApp()
    fun getUserEmail() = userPreferencesRepository.getUserEmail()
    fun getUserFirstName() = userPreferencesRepository.getUserFirstName()
    fun getUserLastName() = userPreferencesRepository.getUserLastName()
    fun getUserId() = userPreferencesRepository.getUserid()
    fun getUserIsAdmin() = userPreferencesRepository.getUserIsAdmin()
    fun getUserRolCode() = userPreferencesRepository.getUserRolCode()
    fun getUserRolId() = userPreferencesRepository.getUserRolId()
    fun getUserRolName() = userPreferencesRepository.getUserRoleName()
    fun getRefreshToken() = userPreferencesRepository.getRefreshToken()
    fun clear() = userPreferencesRepository.clear()
}
