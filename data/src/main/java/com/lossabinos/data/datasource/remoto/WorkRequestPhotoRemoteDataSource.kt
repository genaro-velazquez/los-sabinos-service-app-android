package com.lossabinos.data.datasource.remoto

import com.lossabinos.data.dto.responses.UploadPhotoResponseDTO
import com.lossabinos.data.dto.repositories.retrofit.RetrofitResponseValidator
import com.lossabinos.data.dto.responses.GetNotificationsResponseDTO
import com.lossabinos.data.dto.utilities.HeadersMaker
import com.lossabinos.data.retrofit.SyncServices
import com.lossabinos.data.retrofit.WorkRequestServices
import com.lossabinos.domain.responses.UploadPhotoResponse
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import java.io.File
import javax.inject.Inject

class WorkRequestPhotoRemoteDataSource @Inject constructor(
    private val workRequestServices: WorkRequestServices,
    private val headersMaker: HeadersMaker
) {

    suspend fun uploadPhoto(
        serviceId: String,
        photoFile: File,
        description: String = ""
    ) : UploadPhotoResponseDTO {
        // Validar que el archivo existe
        if (!photoFile.exists()) {
            throw Exception("Archivo no encontrado: ${photoFile.absolutePath}")
        }

        println("📸 [DataSource] Enviando foto: ${photoFile.name}")
        println("   - Tamaño: ${photoFile.length()} bytes")
        println("   - Descripción: $description")
        println("   - serviceId: $serviceId")

        // Crear el MultipartBody.Part para el archivo
        val filePart = MultipartBody.Part.createFormData(
            "file",                    // nombre del parámetro
            photoFile.name,           // nombre del archivo
            photoFile.asRequestBody("image/jpeg".toMediaType())  // tipo MIME
        )

        // Crear RequestBody para description
        val descriptionBody = description.toRequestBody("text/plain".toMediaType())

        // Enviar
        val response = workRequestServices.workRequestPhoto(
            headers = headersMaker.build(),
            serviceExecutionId = serviceId,
            file = filePart,
            description = descriptionBody
        )
        val json = RetrofitResponseValidator.validate(response = response)
        return UploadPhotoResponseDTO(json = json)
    }

}