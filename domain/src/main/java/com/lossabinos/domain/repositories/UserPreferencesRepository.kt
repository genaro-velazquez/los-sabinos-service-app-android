package com.lossabinos.domain.repositories

interface UserPreferencesRepository {
    // app
    fun getPrimaryColor():String
    fun setPrimaryColor(color:String)
    fun getSecondaryColor(): String
    fun setSecondaryColor(color:String)
    fun getNameApp():String
    fun setNameApp(name:String)
    // User
    fun getUserEmail():String
    fun setUserEmail(email:String)
    fun getUserFirstName():String
    fun setUserFirstName(firstName:String)
    fun getUserLastName(): String
    fun setUserLastName(lastName:String)
    fun getUserid():String
    fun setUserid(id:String)
    fun getUserIsAdmin(): Boolean
    fun setUserIsAdmin(isAdmin: Boolean)
    fun getUserRolCode():String
    fun setUserRolCode(rolCode:String)
    fun getUserRolId():String
    fun setUserRolId(rolId:String)
    fun getUserRoleName():String
    fun setUserRoleName(rolName: String)
    fun getRefreshToken():String
    fun setRefreshToken(refreshToken:String)
    fun getIslogged(): Boolean
    fun setIsLogged(islogged: Boolean)
    fun clear()
}