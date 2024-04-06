package com.note.mynote

import android.content.Context
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.note.mynote.db.AppDatabase
import com.note.mynote.db.NoteDao
import com.note.mynote.models.Note
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.AfterClass
import org.junit.Assert
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NoteDataBaseTest {
    companion object {
        private lateinit var context: Context
        private lateinit var database: AppDatabase
        private lateinit var dao: NoteDao

        @BeforeClass
        @JvmStatic
        fun setupClass() {
            context = InstrumentationRegistry.getInstrumentation().context
            database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
            dao = database.noteDao()
        }

        @AfterClass
        @JvmStatic
        fun teardownClass() {
            database.close()
        }
    }

    @Before
    fun setup() {
        database.clearAllTables()
    }

    /**
     * Create note test
     * Parameter: Note Value: Valid
     * Result: Successfully create note in db
     */
    @Test
    fun testInsertNote_Note_NoteCreateSuccessfully() {
        runBlocking {
            try {
                //Arrange
                val note = Note(
                    "fgfff",
                    "dfgdfgdfg",
                    "dfgdfg",
                    "sdfsdf"
                )
                CoroutineScope(Dispatchers.IO).launch {
                    //Act
                    dao.insertNote(note)
                    //Assert
                    dao.noteList().collect{
                        Assert.assertTrue(it.isNotEmpty())
                    }
                }

            } catch (e: Exception) {
                Assert.fail(e.message)
            }
        }
    }

    /**
     * Update note test
     * Parameter: Note Value: Valid
     * Result: Successfully update note in db
     */
    @Test
    fun testUpdateNote_Note_PropertyUpdateSuccessfully() {
        runBlocking {
            try {
                //Arrange
                val note = Note(
                    120,
                    "fgfff",
                    "dfgdfgdfg",
                    "dfgdfg",
                    "sdfsdf"
                )

                CoroutineScope(Dispatchers.IO).launch {
                    //Act
                    dao.insertNote(note)
                    dao.updateNote(
                        Note(
                            120,
                            "serahi",
                            "1389",
                            "22:40",
                            "04/06/2024"
                        )
                    )
                    //Assert
                    dao.noteList().collect{
                        Assert.assertTrue(it.first().title == "serahi")
                    }
                }

            } catch (e: Exception) {
                Assert.fail(e.message)
            }
        }
    }

    /**
     * Get note list test
     * Parameter: No parameter Value: No parameter
     * Result: Successfully get note list from db
     */
    @Test
    fun testGetNoteList_NoParameter_GetNoteListSuccessfully() {
        runBlocking {
            try {
                //Arrange
                val note = Note(
                    "fgfff",
                    "dfgdfgdfg",
                    "dfgdfg",
                    "sdfsdf"
                )
                CoroutineScope(Dispatchers.IO).launch {
                    //Act
                    dao.insertNote(note)
                    //Assert
                    dao.noteList().collect{
                        Assert.assertTrue(it.isNotEmpty())
                    }
                }

            } catch (e: Exception) {
                Assert.fail(e.message)
            }
        }
    }

    /**
     * Delete note test
     * Parameter: Note Value: Valid note id
     * Result: Successfully delete note
     */
    @Test
    fun testDeleteNote_ValidNoteId_SuccessfullyDeleteNote() {
       runBlocking {
           try {
               //Arrange
               val note = Note(
                   100,
                   "fgfff",
                   "dfgdfgdfg",
                   "dfgdfg",
                   "sdfsdf"
               )
               CoroutineScope(Dispatchers.IO).launch {
                   //Act
                   dao.insertNote(note)
                   dao.deleteNote(note)
                   //Assert
                   dao.noteList().collect{noteList->
                       Assert.assertTrue(noteList.none { it.id == note.id })
                   }
               }

           } catch (e: Exception) {
               Assert.fail(e.message)
           }
       }
    }

    /**
     * Delete all notes test
     * Parameter: No parameter Value: No parameter
     * Result: Successfully delete all notes
     */
    @Test
    fun testDeleteAllNotes_NoParameter_SuccessfullyDeleteAllNotes() {
        runBlocking {
            try {
                //Arrange
                val note = Note(
                    100,
                    "fgfff",
                    "dfgdfgdfg",
                    "dfgdfg",
                    "sdfsdf"
                )
                CoroutineScope(Dispatchers.IO).launch {
                    //Act
                    dao.insertNote(note)
                    dao.deleteAllNotes()
                    //Assert
                    dao.noteList().collect{
                        Assert.assertTrue(it.isEmpty())
                    }
                }
            } catch (e: Exception) {
                Assert.fail(e.message)
            }
        }
    }
}