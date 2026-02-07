package com.lossabinos.domain.repositories

import com.lossabinos.domain.valueobjects.WorkRequest
import com.lossabinos.domain.valueobjects.WorkRequestIssue

interface IssueRepository {
    /**
     * Crea un Issue remoto a partir de un WorkRequest
     * y regresa el issueId remoto.
     */
    suspend fun createIssue(
        serviceExecutionId: String,
        issue: WorkRequestIssue
    ): String
}