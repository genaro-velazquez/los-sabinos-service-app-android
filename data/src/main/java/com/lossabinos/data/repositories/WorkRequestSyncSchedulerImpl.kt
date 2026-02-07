package com.lossabinos.data.repositories

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.lossabinos.data.worker.WorkRequestSyncWorker
import com.lossabinos.domain.repositories.WorkRequestSyncScheduler
import javax.inject.Inject
import dagger.hilt.android.qualifiers.ApplicationContext

class WorkRequestSyncSchedulerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : WorkRequestSyncScheduler {

    override fun enqueue() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val request = OneTimeWorkRequestBuilder<WorkRequestSyncWorker>()
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(context)
            .enqueueUniqueWork(
                "work_request_sync",
                ExistingWorkPolicy.KEEP,
                request
            )
    }

}