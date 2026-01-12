package com.notey.app.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Note entity for Room database.
 */
@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val content: String,
    val createdAt: Long = System.currentTimeMillis(),
    val reminderTime: Long? = null, // Null if no reminder set
    val color: Int = 0, // Index of color in predefined list (0 = default)
    val categoryId: Int? = null // For future categories feature
)

/**
 * Predefined note colors for the color picker.
 */
object NoteColors {
    val colors = listOf(
        0xFF424242, // Default Gray
        0xFFE53935, // Red
        0xFFD81B60, // Pink
        0xFF8E24AA, // Purple
        0xFF5E35B1, // Deep Purple
        0xFF3949AB, // Indigo
        0xFF1E88E5, // Blue
        0xFF00ACC1, // Cyan
        0xFF00897B, // Teal
        0xFF43A047, // Green
        0xFFC0CA33, // Lime
        0xFFFFB300  // Amber
    )
    
    fun getColor(index: Int): Long {
        return colors.getOrElse(index) { colors[0] }
    }
}
