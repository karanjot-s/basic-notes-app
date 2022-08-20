package com.karanjotsingh.basicnotes.data

import androidx.room.Entity
import androidx.room.PrimaryKey

const val databaseName = "notes"

@Entity(tableName = databaseName)
class Note (
    var title: String,
    val note: String,
    var pinned: Boolean = false,
    @PrimaryKey(autoGenerate = true) val id: Long = 0L
        )
