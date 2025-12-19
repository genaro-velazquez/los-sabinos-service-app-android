package com.lossabinos.serviceapp.utils

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale


object DateUtils {

    /**
     * Formatea una fecha ISO a formato legible
     * Ej: "2025-11-29T20:00:00Z" → "29 Nov 2025, 20:00"
     */
    fun formatIsoToReadable(isoDateTime: String): String {
        return try {
            // Parsear desde formato ISO
            val zonedDateTime = ZonedDateTime.parse(isoDateTime)
            val localDateTime = zonedDateTime.toLocalDateTime()

            // Crear formato deseado
            val formatter = DateTimeFormatter.ofPattern(
                "dd MMM yyyy, HH:mm",
                Locale("es", "ES")  // Para que diga "29 nov" en español
            )

            localDateTime.format(formatter)
        } catch (e: Exception) {
            isoDateTime  // Si hay error, devuelve el original
        }
    }

    /**
     * Formatea solo la hora
     * Ej: "2025-11-29T20:00:00Z" → "20:00"
     */
    fun formatIsoToTime(isoDateTime: String): String {
        return try {
            val zonedDateTime = ZonedDateTime.parse(isoDateTime)
            val localDateTime = zonedDateTime.toLocalDateTime()

            val formatter = DateTimeFormatter.ofPattern("HH:mm")
            localDateTime.format(formatter)
        } catch (e: Exception) {
            isoDateTime
        }
    }

    /**
     * Formatea solo la fecha
     * Ej: "2025-11-29T20:00:00Z" → "29 Nov 2025"
     */
    fun formatIsoToDate(isoDateTime: String): String {
        return try {
            val zonedDateTime = ZonedDateTime.parse(isoDateTime)
            val localDateTime = zonedDateTime.toLocalDateTime()

            val formatter = DateTimeFormatter.ofPattern(
                "dd MMM yyyy",
                Locale("es", "ES")
            )
            localDateTime.format(formatter)
        } catch (e: Exception) {
            isoDateTime
        }
    }

    /**
     * Calcula la duración entre dos fechas ISO
     * Ej: "2025-11-29T20:00:00Z" a "2025-12-17T21:32:00Z" → "18d 1h 32m"
     */
    fun calculateDuration(startTime: String, endTime: String): String {
        return try {
            val start = ZonedDateTime.parse(startTime)
            val end = ZonedDateTime.parse(endTime)

            val days = ChronoUnit.DAYS.between(start, end)
            val hours = ChronoUnit.HOURS.between(start, end) % 24
            val minutes = ChronoUnit.MINUTES.between(start, end) % 60

            when {
                days > 0 -> "${days}d ${hours}h ${minutes}m"
                hours > 0 -> "${hours}h ${minutes}m"
                minutes > 0 -> "${minutes}m"
                else -> "0m"
            }
        } catch (e: Exception) {
            "N/A"
        }
    }

}
