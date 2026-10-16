package com.notey.app.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Room Database for Notey app.
 */
@Database(
    entities = [Note::class, Category::class], 
    version = 2, 
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
    abstract fun categoryDao(): CategoryDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "notey_database"
                )
                .fallbackToDestructiveMigration(dropAllTables = true) // Simple migration for dev
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
