package com.lossabinos.data.repositories

import com.lossabinos.data.datasource.remoto.IssueRemoteDataSource
import com.lossabinos.data.mappers.WorkRequestEntityMapper
import com.lossabinos.data.mappers.WorkRequestIssueApiMapper
import com.lossabinos.domain.repositories.IssueRepository
import com.lossabinos.domain.valueobjects.WorkRequest
import com.lossabinos.domain.valueobjects.WorkRequestIssue

class IssueRepositoryImpl(
    private val remoteDataSource: IssueRemoteDataSource,
    private val apiMapper: WorkRequestIssueApiMapper,
) : IssueRepository {

    override suspend fun createIssue(
        serviceExecutionId: String,
        issue: WorkRequestIssue
    ): String {

        val body = apiMapper.toRequestBody(issue)
        return remoteDataSource.createIssue(
            serviceExecutionId = serviceExecutionId,
            body = body)

    }

}