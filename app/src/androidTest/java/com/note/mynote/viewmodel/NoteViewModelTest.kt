package com.note.mynote.viewmodel

import android.content.Context
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.note.mynote.data.local.AppDatabase
import com.note.mynote.data.local.NoteDao
import com.note.mynote.data.models.Note
import com.note.mynote.viewmodel.repository.FakeRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

class NoteViewModelTest {

    companion object{
        private lateinit var context: Context
        private lateinit var dataBase : AppDatabase
        private lateinit var dao: NoteDao
        private lateinit var viewModel: NoteViewModel

        @JvmStatic
        @BeforeClass
        fun setupClass(){
            context = InstrumentationRegistry.getInstrumentation().targetContext
            dataBase = Room.inMemoryDatabaseBuilder(context,AppDatabase::class.java).build()
            dao = dataBase.noteDao()
            viewModel = NoteViewModel(FakeRepository(dao))
        }

    }

    @Before
    fun setUp() {
        dataBase.clearAllTables()
    }

    /**
     * Insert note test
     * input: note value: valid
     * Result: Note added to database
     */
    @Test
    fun testInsertNote_NoteParameter_AddNoteToDatabase() = runBlocking {
        try {
            //Arrange
            val note = Note(
                id = 100,
                title = "Car",
                description = "Nice blue car..."
            )
            //Act
            viewModel.upsertNote(note)
            delay(3000)
            //Assert
            assertTrue(dao.noteList().first().first() == note)
        }catch (e : Exception){
            fail(e.message)
        }
    }

    /**
     * Update note test
     * input: note value: valid
     * Result: Note update in database
     */
    @Test
    fun testUpdateNote_NoteParameter_UpdateNoteInDatabase() = runBlocking {
        try {
            //Arrange
            val note = Note(
                id = 100,
                title = "Car",
                description = "Nice blue car..."
            )
            val updatedNote = Note(
                id = 100,
                title = "Motorcycle",
                description = "Nice blue motorCycle..."
            )
            dao.upsertNote(note)
            //Act
            viewModel.upsertNote(updatedNote)
            delay(3000)
            //Assert
            val firstRecord = dao.noteList().first().first()
            assertTrue(firstRecord.title == updatedNote.title && firstRecord.description == updatedNote.description)
        }catch (e : Exception){
            fail(e.message)
        }
    }

    /**
     * Get note list test
     * input: no value: no
     * Result: Get note list
     */
    @Test
    fun testGetNoteList_NoParameter_GetNoteList() = runBlocking {
        try {
            //Arrange
            val note = Note(
                id = 100,
                title = "Car",
                description = "Nice blue car..."
            )
            dao.upsertNote(note)
            delay(3000)
            //Act&Assert
            assertTrue(viewModel.getNoteList.first().isNotEmpty())
        }catch (e : Exception){
            fail(e.message)
        }
    }

    /**
     * Delete note test
     * input: note value: valid
     * Result: Deleted note not be exist
     */
    @Test
    fun testDeleteNote_NoteParameter_DeletedNoteNotBeExist() = runBlocking {
        try {
            //Arrange
            val note = Note(
                id = 100,
                title = "Car",
                description = "Nice blue car..."
            )
            dao.upsertNote(note)
            //Act
            viewModel.deleteNote(note)
            delay(3000)
            //Assert
            assertTrue(!dao.noteList().first().contains(note))
        }catch (e : Exception){
            fail(e.message)
        }
    }

    /**
     * Delete all notes test
     * input: no value: no
     * Result: Deleted note not be exist
     */
    @Test
    fun testDeleteNotes_NoParameter_ListMustBeEmpty() = runBlocking {
        try {
            //Arrange
            val note1 = Note(
                id = 100,
                title = "Car",
                description = "Nice blue car..."
            )
            val note2 = Note(
                id = 101,
                title = "Car",
                description = "Nice blue car..."
            )
            dao.upsertNote(note1)
            dao.upsertNote(note2)
            //Act
            viewModel.deleteNotes()
            delay(3000)
            //Assert
            assertTrue(dao.noteList().first().isEmpty())
        }catch (e : Exception){
            fail(e.message)
        }
    }

    /**
     * Get specific note test
     * input: noteID value: valid
     * Result: Note must be valid
     */
    @Test
    fun testGetSpecificNote_NoteID_NoteMustBeValid() = runBlocking {
        try {
            //Arrange
            val note1 = Note(
                id = 100,
                title = "Car",
                description = "Nice blue car..."
            )
            val note2 = Note(
                id = 101,
                title = "Car",
                description = "Nice blue car..."
            )
            dao.upsertNote(note1)
            dao.upsertNote(note2)
            delay(3000)
            //Act
            val specificNote = viewModel.getSpecificNote(note2.id)
            //Arrange
            assertEquals(note2,specificNote)
        }catch (e : Exception){
            fail(e.message)
        }
    }
}