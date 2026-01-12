package com.notey.app.reminder

import android.content.Context
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

/**
 * Schedules and manages reminders using WorkManager.
 */
object ReminderScheduler {
    
    fun scheduleReminder(
        context: Context,
        noteId: Int,
        title: String,
        content: String,
        delayMillis: Long
    ) {
        val data = Data.Builder()
            .putInt("noteId", noteId)
            .putString("title", title)
            .putString("content", content)
            .build()
        
        val request = OneTimeWorkRequestBuilder<ReminderWorker>()
            .setInitialDelay(delayMillis, TimeUnit.MILLISECONDS)
            .setInputData(data)
            .addTag("reminder_$noteId")
            .build()
        
        WorkManager.getInstance(context).enqueue(request)
    }
    
    fun cancelReminder(context: Context, noteId: Int) {
        WorkManager.getInstance(context).cancelAllWorkByTag("reminder_$noteId")
    }
}
