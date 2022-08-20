package com.karanjotsingh.basicnotes.ui

import android.app.Application
import androidx.lifecycle.*
import com.karanjotsingh.basicnotes.data.Note
import com.karanjotsingh.basicnotes.data.NotesRepository
import kotlinx.coroutines.launch

class NoteDetailsViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = NotesRepository(application)

    private val _noteId = MutableLiveData<Long>(0)
    val noteId: LiveData<Long> get() = _noteId

    var pinned: Boolean = false

    val note: LiveData<Note> = Transformations.switchMap(_noteId) { id ->
        val temp = repo.getNote(id)
        temp.value?.let { pinned = it.pinned }
        temp
    }

    fun togglePinned() {
        pinned = !pinned
        note.value?.let {
            saveNote(Note(it.title, it.note, pinned, it.id))
        }
    }

    fun setNoteId(id: Long) {
        if (_noteId.value != id) {
            _noteId.value = id
        }
    }

    fun saveNote(note: Note) {
        viewModelScope.launch {
            if (note.id == 0L) {
                repo.addNote(note)
            } else {
                repo.updateNote(note)
            }
        }
    }

    fun deleteNote() {
        viewModelScope.launch {
            note.value?.let {
                repo.deleteNote(it)
            }
        }
    }
}
