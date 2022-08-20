package com.karanjotsingh.basicnotes.data

import android.app.Application
import androidx.lifecycle.LiveData

class NotesRepository(context: Application) {
    private val notesDao = NotesDatabase.getDatabase(context).notesDao

    fun getNote(id: Long): LiveData<Note> {
        return notesDao.getNote(id)
    }

    fun getNotes(): LiveData<List<Note>> {
        return notesDao.getNotes()
    }

    suspend fun addNote(note: Note): Long {
        return notesDao.insertNote(note)
    }

    suspend fun updateNote(note: Note) {
        notesDao.updateNote(note)
    }

    suspend fun deleteNote(note: Note) {
        notesDao.deleteNote(note)
    }
}
