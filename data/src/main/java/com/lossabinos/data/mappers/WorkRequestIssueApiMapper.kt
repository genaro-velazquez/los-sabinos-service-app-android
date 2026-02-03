package com.lossabinos.data.mappers

import com.lossabinos.domain.valueobjects.WorkRequestIssue
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import javax.inject.Inject


class WorkRequestIssueApiMapper @Inject constructor(

) {

    fun toRequestBody(issue: WorkRequestIssue): RequestBody {
        val json = JSONObject().apply {
            put("issue_description", issue.description)
            put("severity", issue.severity.name.lowercase())
            put("category", issue.category.name.lowercase())
        }

        return json.toString()
            .toRequestBody("application/json".toMediaType())
    }
}
