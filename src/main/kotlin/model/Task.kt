package model

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

data class Task(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val completed: Boolean = false,
    val createdAt: LocalDateTime = LocalDateTime.now()
) {
    /**
     * Convert to template context.
     * REQUIRED for Pebble rendering in all route handlers.
     * Used as: tasks.map { it.toPebbleContext() }
     */
    fun toPebbleContext(): Map<String, Any> = mapOf(
        "id" to id,
        "title" to title,
        "completed" to completed,
        "createdAt" to createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
        "createdAtISO" to createdAt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    )
}
