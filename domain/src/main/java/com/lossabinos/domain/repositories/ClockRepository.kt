package com.lossabinos.domain.repositories

import java.time.Instant

interface ClockRepository {
    fun now(): Instant
}
