package com.lossabinos.data.repositories

import com.lossabinos.data.datasource.local.WorkRequestPhotoLocalDataSource
import com.lossabinos.data.datasource.local.database.dao.WorkRequestPhotoDao
import com.lossabinos.data.datasource.local.database.entities.SyncStatusEntity
import com.lossabinos.data.datasource.remoto.WorkRequestPhotoRemoteDataSource
import com.lossabinos.data.mappers.WorkRequestPhotoEntityMapper
import com.lossabinos.data.mappers.toDomain
import com.lossabinos.data.mappers.toEntity
import com.lossabinos.domain.entities.Photo
import com.lossabinos.domain.entities.WorkRequestPhoto
import com.lossabinos.domain.enums.SyncStatus
import com.lossabinos.domain.repositories.WorkRequestPhotoRepository
import com.lossabinos.domain.responses.UploadPhotoResponse
import com.lossabinos.domain.valueobjects.UploadedPhoto
import java.io.File

class WorkRequestPhotoRepositoryImpl(
    private val workRequestPhotoLocalDataSource: WorkRequestPhotoLocalDataSource,
    private val workRequestPhotoRemoteDataSource: WorkRequestPhotoRemoteDataSource,
    private val mapper: WorkRequestPhotoEntityMapper
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
        println("delete photo photoId:$photoId")
        workRequestPhotoLocalDataSource.deletePhoto(photoId)
    }

    override suspend fun uploadPhotos(
        serviceExecutionId: String,
        photos: List<WorkRequestPhoto>
    ): List<UploadedPhoto> {
        return photos.map { photo ->
            val response = workRequestPhotoRemoteDataSource.uploadPhoto(
                serviceId = serviceExecutionId,
                photoFile = File(photo.localPath)
            )

            // mapear DTO → dominio
            UploadedPhoto(
                localPhotoId = photo.id,
                remotePhotoId = response.photo.id,
                remoteUrl = response.photo.url,
                uploadedAt = response.photo.uploadedAt
            )
        }

        /*
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
        */
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

    override suspend fun markAsSynced(
        uploadedPhotos: List<UploadedPhoto>
    ) {
        workRequestPhotoLocalDataSource.markAsSynced(
            uploadedPhotos = uploadedPhotos
        )
    }

/*
    override suspend fun markPhotosAsSynced(
        photos: List<WorkRequestPhoto>,
        uploadedPhotos: List<Photo>
    ) {
        workRequestPhotoLocalDataSource.markAsSynced(
            uploadedPhotos = uploadedPhotos
        )
    }
*/

    override suspend fun registerAll(photos: List<WorkRequestPhoto>) {
        workRequestPhotoLocalDataSource.savePhotos(
            photos = photos.map {
                it.copy(syncStatus = SyncStatus.PENDING).toEntity()
            }
        )
    }

    override suspend fun getPendingByWorkRequest(workRequestId: String): List<WorkRequestPhoto> {
        return workRequestPhotoLocalDataSource
            .getByWorkRequestAndStatus(
                workRequestId = workRequestId,
                status = SyncStatusEntity.PENDING
            )
            .map { mapper.toDomain(it) }
    }
}
