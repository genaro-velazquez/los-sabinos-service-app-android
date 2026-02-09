package com.lossabinos.data.mappers

import com.lossabinos.domain.valueobjects.WorkRequest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject

class WorkRequestApiMapper @Inject constructor(

) {

    fun toRequestBody(
        workRequest: WorkRequest,
        photoUrls: List<String>
    ): RequestBody {

        val json = JSONObject().apply {
            put("work_order_id", workRequest.workOrderId)
            put("title", workRequest.title)
            put("description", workRequest.description)
            put("findings", workRequest.findings)
            put("justification", workRequest.justification)
            put("photo_urls", JSONArray(photoUrls))
            put("request_type", workRequest.requestType)
            put("requires_customer_approval", workRequest.requiresCustomerApproval)
            put("vehicle_id", workRequest.vehicleId)
            put("urgency", workRequest.urgency.name.lowercase())


            //put("issue_id", issueId)
            //put("service_execution_id", workRequest.serviceExecutionId)
            //put("created_at", workRequest.createdAt)
        }

        return json
            .toString()
            .toRequestBody("application/json".toMediaType())
    }
}
