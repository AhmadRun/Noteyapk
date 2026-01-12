package com.notey.app.backup

import android.content.Context
import android.net.Uri
import com.notey.app.data.Note
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.BufferedReader

/**
 * Data class for backup file structure.
 */
@Serializable
data class BackupData(
    val notes: List<NoteBackup>,
    val exportedAt: Long = System.currentTimeMillis()
)

@Serializable
data class NoteBackup(
    val title: String,
    val content: String,
    val createdAt: Long,
    val color: Int,
    val reminderTime: Long?
)

/**
 * Handles backup and restore of notes.
 */
object BackupManager {
    
    private val json = Json { prettyPrint = true }
    
    fun exportToJson(notes: List<Note>): String {
        val backupNotes = notes.map { 
            NoteBackup(
                title = it.title,
                content = it.content,
                createdAt = it.createdAt,
                color = it.color,
                reminderTime = it.reminderTime
            )
        }
        return json.encodeToString(BackupData(backupNotes))
    }
    
    fun importFromJson(jsonString: String): List<Note> {
        val backup = json.decodeFromString<BackupData>(jsonString)
        return backup.notes.map {
            Note(
                title = it.title,
                content = it.content,
                createdAt = it.createdAt,
                color = it.color,
                reminderTime = it.reminderTime
            )
        }
    }
    
    fun writeToUri(context: Context, uri: Uri, content: String): Boolean {
        return try {
            context.contentResolver.openOutputStream(uri)?.use { stream ->
                stream.write(content.toByteArray())
            }
            true
        } catch (e: Exception) {
            false
        }
    }
    
    fun readFromUri(context: Context, uri: Uri): String? {
        return try {
            context.contentResolver.openInputStream(uri)?.bufferedReader()?.use { it.readText() }
        } catch (e: Exception) {
            null
        }
    }
}
