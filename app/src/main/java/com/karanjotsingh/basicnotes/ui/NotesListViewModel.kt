package com.karanjotsingh.basicnotes.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.karanjotsingh.basicnotes.data.Note
import com.karanjotsingh.basicnotes.data.NotesRepository
import kotlinx.coroutines.launch

class NotesListViewModel(application: Application): AndroidViewModel(application) {
    private val repo = NotesRepository(application)

    val notesList = repo.getNotes()


    fun testInsertNote() {
        viewModelScope.launch {
            repo.addNote(Note("Test Note 1", "This is first test note."))
            repo.addNote(Note("Test Note 2", "This is second test note."))
            repo.addNote(Note("Test Note 3", "This is third test note."))
        }
    }

}
