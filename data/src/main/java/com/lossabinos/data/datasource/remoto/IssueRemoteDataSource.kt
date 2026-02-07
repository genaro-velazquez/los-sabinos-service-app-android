package com.lossabinos.data.datasource.remoto

import com.lossabinos.data.dto.repositories.retrofit.RetrofitResponseValidator
import com.lossabinos.data.dto.utilities.HeadersMaker
import com.lossabinos.data.dto.utilities.asBoolean
import com.lossabinos.data.dto.utilities.asJSONObject
import com.lossabinos.data.dto.utilities.asString
import com.lossabinos.data.retrofit.WorkRequestServices
import okhttp3.RequestBody
import javax.inject.Inject

class IssueRemoteDataSource @Inject constructor(
    private val headersMaker: HeadersMaker,
    private val service: WorkRequestServices
) {

    suspend fun createIssue(
        serviceExecutionId: String,
        body: RequestBody
    ): String {
        val response = service.workRequestIssue(
            headers = headersMaker.build(),
            serviceExecutionId = serviceExecutionId,
            request = body
        )
        val json = RetrofitResponseValidator.validate(response = response)
        val id = json.asJSONObject("data").asJSONObject("finding").asString("id")
        return id
    }

}