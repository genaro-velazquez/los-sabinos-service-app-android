package com.lossabinos.domain.usecases.preferences

import com.lossabinos.domain.repositories.UserPreferencesRepository

class SetUserPreferencesUseCase(private val userPreferencesRepository: UserPreferencesRepository) {
    fun setPrimaryColor(color: String)          = userPreferencesRepository.setPrimaryColor(color = color)
    fun setSecondaryColor(color: String)        = userPreferencesRepository.setSecondaryColor(color = color)
    fun setNameApp(name:String)                 = userPreferencesRepository.setNameApp(name = name)
    fun setUserEmail(email:String)              = userPreferencesRepository.setUserEmail(email = email)
    fun setUserFirstName(firstName:String)      = userPreferencesRepository.setUserFirstName(firstName = firstName)
    fun setUserLastName(lastName:String)        = userPreferencesRepository.setUserLastName(lastName = lastName)
    fun setUserId(id:String)                    = userPreferencesRepository.setUserid(id = id)
    fun setUserIsAdmin(isAdmin: Boolean)        = userPreferencesRepository.setUserIsAdmin(isAdmin = isAdmin)
    fun setUserRolCode (rolCode: String)        = userPreferencesRepository.setUserRolCode(rolCode = rolCode)
    fun setUserRolId(rolId:String)              = userPreferencesRepository.setUserRolId(rolId = rolId)
    fun setUserRolName(rolName:String)          = userPreferencesRepository.setUserRoleName(rolName = rolName)
    fun setRefreshToken(refreshToken:String)    = userPreferencesRepository.setRefreshToken(refreshToken = refreshToken)
    fun setAccessToken(token: String)           = userPreferencesRepository.setAccessToken(token = token)
}