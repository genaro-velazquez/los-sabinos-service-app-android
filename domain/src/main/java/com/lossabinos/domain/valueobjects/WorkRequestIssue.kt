package com.lossabinos.domain.valueobjects

import com.lossabinos.domain.enums.IssueCategoryType
import com.lossabinos.domain.enums.SeverityLevelType
import com.lossabinos.domain.enums.UrgencyLevelType


data class WorkRequestIssue(
    val description: String,
    val severity: UrgencyLevelType,
    val category: IssueCategoryType
)
