package com.example.iverify.devices.util

import java.time.Duration
import java.time.Instant

// Define an extension function on the Instant class to convert a timestamp to a human-readable time ago format
fun Instant.toHumanReadableTimeAgo(): String {
    // Get the current instant
    val now = Instant.now()

    // Calculate the duration between the provided instant and the current instant
    val duration = Duration.between(this, now)

    // Determine the appropriate time unit (days, hours, minutes, or "just now") and format the result accordingly
    return when {
        duration.toDays() > 0 -> "${duration.toDays()} days ago"
        duration.toHours() > 0 -> "${duration.toHours()} hours ago"
        duration.toMinutes() > 0 -> "${duration.toMinutes()} minutes ago"
        else -> "just now"
    }
}