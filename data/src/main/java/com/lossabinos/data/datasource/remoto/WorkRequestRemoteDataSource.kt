package com.lossabinos.data.datasource.remoto

import com.lossabinos.data.dto.utilities.HeadersMaker
import com.lossabinos.data.retrofit.WorkRequestServices
import com.lossabinos.domain.valueobjects.WorkRequest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import javax.inject.Inject

class WorkRequestRemoteDataSource @Inject constructor(
    private val headersMaker: HeadersMaker,
    private val workRequestServices: WorkRequestServices
) {
    suspend fun workRequest(
        request: RequestBody
    ){
        workRequestServices.workRequests(
            headers = headersMaker.build(),
            request = request
        )
    }

    suspend fun createIssue(
        serviceExecutionId: String,
        body: RequestBody
    ) {
        workRequestServices.workRequestIssue(
            headers = headersMaker.build(),
            serviceExecutionId = serviceExecutionId,
            request = body
        )
    }

    suspend fun create(
        request: RequestBody
    ){
        workRequestServices.workRequests(
            headers = headersMaker.build(),
            request = request
        )
    }


}