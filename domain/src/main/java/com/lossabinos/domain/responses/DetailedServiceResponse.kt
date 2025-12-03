package com.lossabinos.domain.responses

import com.lossabinos.domain.entities.DomainEntity
import com.lossabinos.domain.entities.ServiceType
import com.lossabinos.domain.valueobjects.CurrentProgress
import com.lossabinos.domain.valueobjects.ServiceInfo
import com.lossabinos.domain.valueobjects.Template

class DetailedServiceResponse(
    val serviceExecutionId: String,
    val serviceId: String,
    val serviceType: ServiceType,
    val template: Template,
    val currentProgress: CurrentProgress,
    val serviceInfo: ServiceInfo
): DomainEntity()