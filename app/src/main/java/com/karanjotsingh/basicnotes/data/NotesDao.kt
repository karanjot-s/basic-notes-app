package com.karanjotsingh.basicnotes.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NotesDao {

    @Query("SELECT * FROM $databaseName ORDER BY pinned DESC")
    fun getNotes(): LiveData<List<Note>>

    @Query("SELECT * FROM $databaseName WHERE id = :id")
    fun getNote(id: Long): LiveData<Note>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertNote(note: Note): Long

    @Update
    suspend fun updateNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)
}
