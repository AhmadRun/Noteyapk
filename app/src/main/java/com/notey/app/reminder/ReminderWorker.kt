package com.notey.app.reminder

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

/**
 * Worker for showing reminder notifications.
 */
class ReminderWorker(
    private val context: Context,
    params: WorkerParameters
) : Worker(context, params) {
    
    override fun doWork(): Result {
        val noteId = inputData.getInt("noteId", 0)
        val title = inputData.getString("title") ?: "Reminder"
        val content = inputData.getString("content") ?: ""
        
        NotificationHelper.showReminderNotification(context, noteId, title, content)
        
        return Result.success()
    }
}
