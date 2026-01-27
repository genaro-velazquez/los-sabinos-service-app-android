package com.lossabinos.data.datasource.remoto

import com.lossabinos.data.dto.repositories.retrofit.RetrofitResponseValidator
import com.lossabinos.data.dto.responses.SignChecklistResponseDTO
import com.lossabinos.data.dto.utilities.HeadersMaker
import com.lossabinos.data.retrofit.SyncServices
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import java.io.File
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

    suspend fun syncProgressEvidence(
        serviceId: String,
        activityId: String,
        photoFile: File,
        photoType: String = "general",
        description: String = ""
    ) : Response<String>{
        // Validar que el archivo existe
        if (!photoFile.exists()) {
            throw Exception("Archivo no encontrado: ${photoFile.absolutePath}")
        }

        println("📸 [DataSource] Enviando foto: ${photoFile.name}")
        println("   - Tamaño: ${photoFile.length()} bytes")
        println("   - Tipo: $photoType")
        println("   - Descripción: $description")
        println("   - serviceId: $serviceId")
        println("   - activityId: $activityId")

        // Crear el MultipartBody.Part para el archivo
        val filePart = MultipartBody.Part.createFormData(
            "file",                    // nombre del parámetro
            photoFile.name,           // nombre del archivo
            photoFile.asRequestBody("image/jpeg".toMediaType())  // tipo MIME
        )

        // Crear RequestBody para photo_type
        val photoTypeBody = photoType.toRequestBody("text/plain".toMediaType())

        // Crear RequestBody para description
        val descriptionBody = description.toRequestBody("text/plain".toMediaType())

        // Enviar
        return syncServices.syncPhotos(
            headers = headersMaker.build(),
            serviceExecutionId = serviceId,
            itemId = activityId,
            file = filePart,
            photoType = photoTypeBody,
            description = descriptionBody
        )
    }

    suspend fun signChecklist(
        serviceId: String,
        request: RequestBody
    ) : SignChecklistResponseDTO{
        val response = syncServices.singChecklist(
            headers = headersMaker.build(),
            serviceExecutionId = serviceId,
            request = request
        )
        val json = RetrofitResponseValidator.validate(response = response)
        return SignChecklistResponseDTO(json = json)
    }

    suspend fun sendReportExtraCosts(
        idExecutionService: String,
        request: RequestBody
    )  {
        syncServices.reportExtraCost(
            headers = headersMaker.build(),
            idServiceExecution = idExecutionService,
            request = request
        )
    }

    suspend fun startService(
        idExecutionService: String,
        request: RequestBody
    ){
        syncServices.startService(
            headers = headersMaker.build(),
            serviceExecutionId = idExecutionService,
            request = request
        )
    }
}