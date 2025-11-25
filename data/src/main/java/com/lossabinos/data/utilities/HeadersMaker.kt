package com.lossabinos.data.dto.utilities

import com.lossabinos.domain.repositories.UserPreferencesRepository

class HeadersMaker(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val language: String
) {

    fun build(): Map<String, String>{
        val map = HashMap<String, String>()

        map["X-LOS-SABINOS-PLATFORM-TYPE"] = "app"
        map["X-LOS-SABINOS-PLATFORM-name"] = "Android"

        return map
    }

}