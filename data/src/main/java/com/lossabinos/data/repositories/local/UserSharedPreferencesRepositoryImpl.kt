package com.lossabinos.data.repositories.local

import android.content.SharedPreferences
import com.lossabinos.domain.repositories.UserPreferencesRepository
import androidx.core.content.edit

class UserSharedPreferencesRepositoryImpl(
    private val sharedPreferences: SharedPreferences
) : UserPreferencesRepository {

    private val primaryColorKey    = "primary_color"
    private val secondaryColorKey  = "secondary_color"
    private val appNameKey         = "app_name"
    private val emailKey           = "email"
    private val userFirstNameKey   = "first_name"
    private val userLastNameKey    = "last_name"
    private val userIdKey          = "user_id"
    private val userIdAdminKey     = "user_is_admin"
    private val userRolCodeKey     = "user_rol_code"
    private val userRolIdKey       = "user_rol_id"
    private val userRolNameKey     = "user_rol_name"
    private val refreshTokenKey    = "refresh_token"

    private fun getStringValue(key: String): String {
        val value = sharedPreferences.getString(key, "")
        if (value != null) {
            return value
        }
        return ""
    }

    override fun getPrimaryColor(): String {
        return getStringValue(primaryColorKey)
    }

    override fun setPrimaryColor(color: String) {
        sharedPreferences.edit {
            putString(primaryColorKey, color)
            apply()
        }
    }

    override fun getSecondaryColor(): String {
        return getStringValue(secondaryColorKey)
    }

    override fun setSecondaryColor(color: String) {
        sharedPreferences.edit{
            putString(secondaryColorKey,color)
            apply()
        }
    }

    override fun getNameApp(): String {
        return getStringValue(appNameKey)
    }

    override fun setNameApp(name: String) {
        sharedPreferences.edit {
            putString(appNameKey,name)
            apply()
        }
    }

    override fun getUserEmail(): String {
        return getStringValue(emailKey)
    }

    override fun setUserEmail(email: String) {
        sharedPreferences.edit {
            putString(emailKey,email)
            apply()
        }
    }

    override fun getUserFirstName(): String {
        return getStringValue(userFirstNameKey)
    }

    override fun setUserFirstName(firstName: String) {
        sharedPreferences.edit {
            putString(firstName,firstName)
            apply()
        }
    }

    override fun getUserLastName(): String {
        return getStringValue(userLastNameKey)
    }

    override fun setUserLastName(lastName: String) {
        sharedPreferences.edit {
            putString(userLastNameKey,lastName)
            apply()
        }
    }

    override fun getUserid(): String {
        return getStringValue(userIdKey)
    }

    override fun setUserid(id: String) {
        sharedPreferences.edit {
            putString(userIdKey,id)
            apply()
        }
    }

    override fun getUserIsAdmin(): Boolean {
        return sharedPreferences.getBoolean(userIdAdminKey, false)
    }

    override fun setUserIsAdmin(isAdmin: Boolean) {
        sharedPreferences.edit {
            putBoolean(userIdAdminKey,isAdmin)
            apply()
        }
    }

    override fun getUserRolCode(): String {
        return getStringValue(userRolCodeKey)
    }

    override fun setUserRolCode(rolCode: String) {
        sharedPreferences.edit {
            putString(userRolCodeKey,rolCode)
            apply()
        }
    }

    override fun getUserRolId(): String {
        return getStringValue(userRolIdKey)
    }

    override fun setUserRolId(rolId: String) {
        sharedPreferences.edit {
            putString(userRolIdKey,rolId)
            apply()
        }
    }

    override fun getUserRoleName(): String {
        return getStringValue(userRolNameKey)
    }

    override fun setUserRoleName(rolName: String) {
        sharedPreferences.edit {
            putString(userRolNameKey,rolName)
            apply()
        }
    }

    override fun getRefreshToken(): String {
        return getStringValue(refreshTokenKey)
    }

    override fun setRefreshToken(refreshToken: String) {
        sharedPreferences.edit {
            putString(refreshTokenKey,refreshToken)
            apply()
        }
    }

    override fun clear() {
        sharedPreferences.edit{
            clear()
            apply()
        }
    }
}
