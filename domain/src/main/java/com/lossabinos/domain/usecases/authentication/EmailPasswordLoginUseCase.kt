package com.lossabinos.domain.usecases.authentication

import com.lossabinos.domain.repositories.AuthenticationRepository
import com.lossabinos.domain.repositories.UserPreferencesRepository
import com.lossabinos.domain.responses.LoginResponse

class EmailPasswordLoginUseCase(
    private val authenticationRepository: AuthenticationRepository,
    userPreferencesRepository: UserPreferencesRepository
): SetUserPreferencesOnLogin(userPreferencesRepository = userPreferencesRepository){
    suspend fun execute(email: String, password: String): LoginResponse{
        val repsonse = authenticationRepository.loginWithEmailAndPassword(
            email = email,
            password = password
        )

        setUserPreferencesOnLogin(response = repsonse)

        return repsonse
    }
}