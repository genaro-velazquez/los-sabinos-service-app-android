package com.lossabinos.serviceapp.utils

import java.time.Clock
import java.time.Instant
import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun Instant.toHomeDateLabel(
    clock: Clock = Clock.systemDefaultZone()
): String {
    val now = LocalDate.now(clock)
    val zonedDateTime = atZone(clock.zone)
    val date = zonedDateTime.toLocalDate()

    val time = zonedDateTime.format(
        DateTimeFormatter.ofPattern("HH:mm")
    )

    return when {
        date == now -> "Hoy $time"
        date == now.minusDays(1) -> "Ayer $time"
        else -> zonedDateTime.format(
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
        )
    }

    /*
        val now = LocalDate.now(clock)
        val date = atZone(clock.zone).toLocalDate()

        return when {
            date == now -> "Hoy"
            date == now.minusDays(1) -> "Ayer"
            else -> date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        }
     */
}
