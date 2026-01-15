package com.lossabinos.data.datasource.remoto

import com.lossabinos.data.dto.utilities.HeadersMaker
import com.lossabinos.data.retrofit.SyncServices
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject

class ChecklistRemoteDataSource @Inject constructor(
    private val syncServices: SyncServices,
    private val headersMaker: HeadersMaker
) {

    suspend fun syncProgress(
        serviceId:String,
        request: RequestBody
    ) : Response<String> {
        return syncServices.syncProgress(
            headers = headersMaker.build(),
            serviceExecutionId = serviceId,
            request = request
        )
    }

}