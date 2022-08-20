package com.karanjotsingh.basicnotes

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.karanjotsingh.basicnotes.data.Note
import com.karanjotsingh.basicnotes.data.NotesDao
import com.karanjotsingh.basicnotes.data.NotesDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class NotesDatabaseTest {
    private lateinit var db: NotesDatabase
    private lateinit var notesDao: NotesDao
    private val rawNotes = listOf(
        Note(
            "Test Note 1",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Vel fringilla est ullamcorper eget nulla facilisi etiam. Porta non pulvinar neque laoreet suspendisse interdum consectetur. Adipiscing bibendum est ultricies integer quis. Adipiscing enim eu turpis egestas pretium."
        ),
        Note(
            "Test Note 2",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Gravida dictum fusce ut placerat orci nulla pellentesque dignissim enim. Scelerisque purus semper eget duis at tellus. Euismod in pellentesque massa placerat duis. Convallis aenean et tortor at risus. Pellentesque sit amet porttitor eget dolor morbi non arcu."
        ),
        Note(
            "Test Note 3",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Eu tincidunt tortor aliquam nulla facilisi cras fermentum odio. Molestie nunc non blandit massa enim nec dui nunc mattis. Ac turpis egestas maecenas pharetra. Eu feugiat pretium nibh ipsum. Nulla aliquet porttitor lacus luctus accumsan. Lorem ipsum dolor sit amet consectetur adipiscing elit."
        ),
        Note(
            "Test Note 4",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Dictum non consectetur a erat nam. Congue quisque egestas diam in arcu cursus. Est sit amet facilisis magna etiam. Facilisi etiam dignissim diam quis. Tristique nulla aliquet enim tortor at auctor. Sed lectus vestibulum mattis ullamcorper velit sed ullamcorper morbi tincidunt. Diam sollicitudin tempor id eu nisl nunc mi."
        ),
        Note(
            "",
            "",
            true
        )
    )
    private val rawNote = rawNotes[0]

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext

        db = Room.inMemoryDatabaseBuilder(context, NotesDatabase::class.java)
            .allowMainThreadQueries().build()
        notesDao = db.notesDao
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun testInsertAndGetNotes() = runBlocking {
        insertMultipleNotes()
        val getNotesResult = notesDao.getNotes()
        for (note in rawNotes) {
            val noteResult = getNotesResult.value?.find { rawNote.id == it.id }
            assertEquals(rawNote.id, noteResult?.id)
        }
    }

    @Test
    fun testUpdateAndGetNote() = runBlocking {
        updateNote()
        val getOneNote = notesDao.getNote(rawNote.id)
        assertEquals(getOneNote.value?.id, rawNote.id)
        assertEquals(getOneNote.value?.title, rawNote.title)
    }

    @Test
    fun testDeleteNote() = runBlocking {
        notesDao.deleteNote(rawNote)
        val deletedNote = notesDao.getNote(rawNote.id)
        Assert.assertNull(deletedNote.value)
    }

    private suspend fun insertMultipleNotes() {
        rawNotes.forEach {
            notesDao.insertNote(it)
        }
    }

    private suspend fun updateNote() {
        rawNote.title = "New updated title"
        notesDao.updateNote(rawNote)
    }
}
