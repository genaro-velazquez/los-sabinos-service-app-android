package com.lossabinos.domain.usecases.authentication

import com.lossabinos.domain.repositories.UserPreferencesRepository
import com.lossabinos.domain.responses.LoginResponse

abstract class SetUserPreferencesOnLogin(
    private val userPreferencesRepository: UserPreferencesRepository
) {
    protected fun setUserPreferencesOnLogin(response: LoginResponse){
        userPreferencesRepository.setPrimaryColor(color = response.primaryColor)
        userPreferencesRepository.setSecondaryColor(color = response.secondaryColor)
        userPreferencesRepository.setNameApp(name = response.appName)
        userPreferencesRepository.setUserEmail(email = response.client.email)
        userPreferencesRepository.setUserFirstName(firstName = response.client.firstName)
        userPreferencesRepository.setUserLastName(lastName = response.client.lastName)
        userPreferencesRepository.setUserid(id = response.client.id)
        userPreferencesRepository.setUserIsAdmin(isAdmin = response.client.isAdmin)
        userPreferencesRepository.setUserRolId(rolId = response.client.userRol.id)
        userPreferencesRepository.setUserRolCode(rolCode = response.client.userRol.code)
        userPreferencesRepository.setUserRoleName(rolName = response.client.userRol.name)
        userPreferencesRepository.setRefreshToken(refreshToken = response.refreshToken)
        userPreferencesRepository.setIsLogged(islogged = true)
    }
}
