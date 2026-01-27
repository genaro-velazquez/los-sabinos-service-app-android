package com.lossabinos.data.repositories

import com.lossabinos.domain.repositories.ClockRepository
import java.time.Instant

class SystemClock : ClockRepository {
    override fun now(): Instant = Instant.now()
}
