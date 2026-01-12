package com.notey.app.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Category entity for organizing notes.
 */
@Entity(tableName = "categories")
data class Category(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val color: Int = 0 // Same color system as notes
)
