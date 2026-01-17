package com.lossabinos.domain.usecases.authentication

import com.lossabinos.domain.repositories.AuthenticationRepository

class GetAccessTokenUseCase(
    private val authenticationRepository: AuthenticationRepository
) {
    fun execute(): String{
        return authenticationRepository.getAccessToken()
    }
}