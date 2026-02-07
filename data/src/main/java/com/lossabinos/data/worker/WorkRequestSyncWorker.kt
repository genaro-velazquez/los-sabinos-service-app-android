package com.lossabinos.data.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.lossabinos.domain.usecases.submitWorkRequestUseCase.SyncWorkRequestUseCase

class WorkRequestSyncWorker(
    context: Context,
    params: WorkerParameters,
    private val syncUseCase: SyncWorkRequestUseCase
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            syncUseCase.execute()
            Result.success()
        } catch (e: Exception) {
            // Cualquier fallo → retry automático
            Result.retry()
        }
    }
}
