package com.lossabinos.data.dto.utilities

import android.os.Build
import com.lossabinos.domain.repositories.UserPreferencesRepository

class HeadersMaker(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val language: String
) {

    fun build(): Map<String, String>{
        val map = HashMap<String, String>()

        map["X-LOS-SABINOS-PLATFORM-TYPE"] = "app"
        map["X-LOS-SABINOS-PLATFORM-name"] = "Android"
        //map["X-App-Version"] = "" //BuildConfig().VERSION_NAME
        map["X-LOS-SABINOS-BUILD-VERSION"] = Build.VERSION.RELEASE
        val token = userPreferencesRepository.getAccessToken() // Obtener del repo
        map["Authorization"] = "Bearer $token"


        return map
    }

}