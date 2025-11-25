package com.lossabinos.data.dto.valueobjects

import android.Manifest
import com.lossabinos.data.dto.entities.DTO
import com.lossabinos.data.dto.utilities.asJSONArray
import com.lossabinos.domain.valueobjects.UserPermissions
import org.json.JSONArray
import org.json.JSONObject

class UserPermissionsDTO (
    json: JSONArray
): DTO<UserPermissions>() {

    val permissions: Array<String> = Array(json.length()){
        index ->
        json.getString(index)
    }

    override fun toEntity(): UserPermissions = UserPermissions(
        permissions = permissions
    )
}