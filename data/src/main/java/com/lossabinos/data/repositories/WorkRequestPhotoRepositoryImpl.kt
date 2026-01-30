package com.lossabinos.data.repositories

import com.lossabinos.data.datasource.local.WorkRequestPhotoLocalDataSource
import com.lossabinos.data.datasource.local.database.dao.WorkRequestPhotoDao
import com.lossabinos.data.datasource.remoto.WorkRequestPhotoRemoteDataSource
import com.lossabinos.data.mappers.toDomain
import com.lossabinos.data.mappers.toEntity
import com.lossabinos.domain.entities.Photo
import com.lossabinos.domain.entities.WorkRequestPhoto
import com.lossabinos.domain.repositories.WorkRequestPhotoRepository
import com.lossabinos.domain.responses.UploadPhotoResponse
import java.io.File

class WorkRequestPhotoRepositoryImpl(
    private val workRequestPhotoLocalDataSource: WorkRequestPhotoLocalDataSource,
    private val workRequestPhotoRemoteDataSource: WorkRequestPhotoRemoteDataSource
) : WorkRequestPhotoRepository {

    override suspend fun savePhoto(photo: WorkRequestPhoto) {
        workRequestPhotoLocalDataSource.savePhoto(photo = photo.toEntity())
    }

    override suspend fun getPhotos(
        workRequestId: String
    ): List<WorkRequestPhoto>
    {
        return workRequestPhotoLocalDataSource
            .getPhotos(workRequestId).map { it.toDomain() }
    }

    override suspend fun deletePhoto(photoId: String) {
        workRequestPhotoLocalDataSource.deletePhoto(photoId)
    }

    override suspend fun uploadPhotos(
        serviceExecutionId: String,
        photos: List<WorkRequestPhoto>
    ): List<Photo> {

        val uploadedPhotos = mutableListOf<Photo>()

        photos.forEach { photo ->
            val response =
                workRequestPhotoRemoteDataSource.uploadPhoto(
                    serviceId = serviceExecutionId,
                    photoFile = File(photo.localPath)
                )

            uploadedPhotos.add(response.toEntity().photo)
        }

        return uploadedPhotos

        /*
                val uploadedPhotos = mutableListOf<Photo>()

                photos.forEach { photo ->

                    // 1️⃣ Validar archivo
                    val file = File(photo.localPath)
                    if (!file.exists()) {
                        throw Exception("Archivo no encontrado: ${photo.localPath}")
                    }

                    // 2️⃣ Subir foto al backend
                    val response = workRequestPhotoRemoteDataSource.uploadPhoto(
                        serviceId = serviceExecutionId,
                        photoFile = file,
                        description = ""
                    )

                    val uploadedPhoto = response.toEntity().photo

                    // 3️⃣ Guardar URL remota + marcar SYNCED
                    workRequestPhotoLocalDataSource.markAsSynced(
                        photoId = photo.id,
                        remoteUrl = uploadedPhoto.url
                    )

                    // 4️⃣ Agregar al resultado
                    uploadedPhotos.add(uploadedPhoto)

                }
                return uploadedPhotos
         */
    }

    override suspend fun markAsSynced(photoId: String, remoteUrl: String) {
        TODO("Not yet implemented")
    }

    override suspend fun markPhotosAsSynced(
        photos: List<WorkRequestPhoto>,
        uploadedPhotos: List<Photo>
    ) {
        photos.forEachIndexed { index, photo ->
            workRequestPhotoLocalDataSource.markAsSynced(
                photoId = photo.id,
                remoteUrl = uploadedPhotos[index].url
            )
        }

    }
}
