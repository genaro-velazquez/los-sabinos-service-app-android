package com.lossabinos.data.mappers

import com.lossabinos.domain.entities.ActivityProgress
import com.lossabinos.domain.entities.ObservationAnswer
import com.lossabinos.domain.entities.ServiceFieldValue
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject

class ChecklistProgressRequestMapper @Inject constructor() {

    fun buildChecklistProgressRequest(
        templateJson: String,
        activities: List<ActivityProgress>,
        observations: List<ObservationAnswer>,
        fields: List<ServiceFieldValue>
    ): JSONObject {
        val templateObject = JSONObject(templateJson)
        val templateData = templateObject.getJSONObject("template")
        //val templateData = templateObject.getJSONObject("template_data")

        val responseJson = JSONObject()
        val sectionsArray = JSONArray()

        // ===== PROCESAR SECTIONS =====
        val sectionsArrayOriginal = templateData.getJSONArray("sections")

        for (sectionIndex in 0 until sectionsArrayOriginal.length()) {
            val sectionOriginal = sectionsArrayOriginal.getJSONObject(sectionIndex)
            val sectionResponse = JSONObject()

            sectionResponse.put("name", sectionOriginal.getString("name"))
            sectionResponse.put("optional", sectionOriginal.getBoolean("optional"))

            if (sectionOriginal.has("metadata")) {
                sectionResponse.put("metadata", sectionOriginal.getJSONArray("metadata"))
            }

            // ===== ACTIVITIES ====
            val activitiesArrayResponse = JSONArray()
            val activitiesOriginal = sectionOriginal.getJSONArray("activities")

            for (activityIndex in 0 until activitiesOriginal.length()) {
                val activityOriginal = activitiesOriginal.getJSONObject(activityIndex)
                val activityResponse = JSONObject()

                activityResponse.put("id", activityOriginal.getString("id"))
                activityResponse.put("description", activityOriginal.getString("description"))
                activityResponse.put("requiresEvidence", activityOriginal.getBoolean("requiresEvidence"))

                val activity = activities.find {
                    it.sectionIndex == sectionIndex && it.activityIndex == activityIndex
                }

                activityResponse.put("valueRequest", activity?.completed ?: false)
                activitiesArrayResponse.put(activityResponse)
            }
            sectionResponse.put("activities", activitiesArrayResponse)

            // ===== OBSERVATIONS =====
            val observationsArrayResponse = JSONArray()
            val observationsOriginal = sectionOriginal.getJSONArray("observations")

            for (observationIndex in 0 until observationsOriginal.length()) {
                val observationOriginal = observationsOriginal.getJSONObject(observationIndex)
                val observationResponse = JSONObject()

                observationResponse.put("id", observationOriginal.getString("id"))
                observationResponse.put("description", observationOriginal.getString("description"))
                observationResponse.put("response_type", observationOriginal.getString("responseType"))
                observationResponse.put("requiresResponse", observationOriginal.getBoolean("requiresResponse"))

                val observation = observations.find {
                    it.sectionIndex == sectionIndex && it.observationIndex == observationIndex
                }

                val responseType = observationOriginal.getString("responseType")
                val value = observation?.answer ?: ""
                val valueRequest = when (responseType) {
                    "boolean" -> value.toBoolean()
                    "number" -> value.toDoubleOrNull() ?: 0
                    else -> value
                }

                observationResponse.put("valueRequest", valueRequest)
                observationsArrayResponse.put(observationResponse)
            }
            sectionResponse.put("observations", observationsArrayResponse)

            sectionsArray.put(sectionResponse)
        }

        responseJson.put("sections", sectionsArray)

        // ===== SERVICE_FIELDS =====
        if (templateData.has("serviceFields")) {
            val fieldsArrayResponse = JSONArray()
            val fieldsOriginal = templateData.getJSONArray("serviceFields")

            for (fieldIndex in 0 until fieldsOriginal.length()) {
                val fieldOriginal = fieldsOriginal.getJSONObject(fieldIndex)
                val fieldLabel = fieldOriginal.getString("label")
                val fieldType = fieldOriginal.getString("type")

                val fieldResponse = JSONObject()
                fieldResponse.put("type", fieldType)
                fieldResponse.put("label", fieldLabel)
                fieldResponse.put("required", fieldOriginal.getBoolean("required"))

                val field = fields.find { it.fieldLabel == fieldLabel }

                val value = when (fieldType) {
                    "number" -> field?.value?.toDoubleOrNull() ?: 0
                    else -> field?.value ?: ""
                }

                fieldResponse.put("valueRequest", value)
                fieldsArrayResponse.put(fieldResponse)
            }

            responseJson.put("serviceFields", fieldsArrayResponse)
        }

        return responseJson
    }
}