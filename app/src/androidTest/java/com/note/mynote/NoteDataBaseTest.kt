package com.note.mynote

import android.content.Context
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.note.mynote.data.local.AppDatabase
import com.note.mynote.data.local.NoteDao
import com.note.mynote.data.models.Note
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
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
            val completableDeferred = CompletableDeferred<Boolean>()
            CoroutineScope(IO).launch {
                try {
                    //Arrange
                    val note = Note(
                        title = "fgfff",
                        description = "dfgdfgdfg",
                        time = "dfgdfg",
                        date = "sdfsdf"
                    )
                    //Act
                    dao.upsertNote(note)
                    dao.noteList().collect {
                        completableDeferred.complete(it.isNotEmpty())
                    }

                } catch (e: Exception) {
                    Assert.fail(e.message)
                }
            }
            //Assert
            Assert.assertTrue(completableDeferred.await())
        }
    }

    /**
     * Update note test
     * Parameter: Note Value: Valid
     * Result: Successfully update note in db
     */
    @Test
    fun testUpdateNote_Note_NoteUpdateSuccessfully() {
        runBlocking {
            val completableDeferred = CompletableDeferred<Boolean>()
            CoroutineScope(IO).launch {
                try {
                    //Arrange
                    val note = Note(
                        120,
                        "fgfff",
                        "dfgdfgdfg",
                        "dfgdfg",
                        "sdfsdf"
                    )
                    //Act
                    dao.upsertNote(note)
                    dao.upsertNote(
                        Note(
                            120,
                            "serahi",
                            "1389",
                            "22:40",
                            "04/06/2024"
                        )
                    )
                    dao.noteList().collect {
                        completableDeferred.complete(it.first().title == "serahi")
                    }
                } catch (e: Exception) {
                    Assert.fail(e.message)
                }
            }
            //Assert
            Assert.assertTrue(completableDeferred.await())
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
            val completableDeferred = CompletableDeferred<Boolean>()
            CoroutineScope(IO).launch {
                try {
                    //Arrange
                    val note = Note(
                        title = "fgfff",
                        description = "dfgdfgdfg",
                        time = "dfgdfg",
                        date = "sdfsdf"
                    )
                    //Act
                    dao.upsertNote(note)
                    dao.noteList().collect {
                        completableDeferred.complete(it.isNotEmpty())
                    }

                } catch (e: Exception) {
                    Assert.fail(e.message)
                }
            }
            //Assert
            Assert.assertTrue(completableDeferred.await())
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
            val completableDeferred = CompletableDeferred<Boolean>()
            CoroutineScope(IO).launch {
                try {
                    //Arrange
                    val note = Note(
                        100,
                        "fgfff",
                        "dfgdfgdfg",
                        "dfgdfg",
                        "sdfsdf"
                    )
                    //Act
                    dao.upsertNote(note)
                    dao.deleteNote(note)
                    dao.noteList().collect { noteList ->
                        completableDeferred.complete(noteList.none { it.id == note.id })
                    }

                } catch (e: Exception) {
                    Assert.fail(e.message)
                }
            }
            //Assert
            Assert.assertTrue(completableDeferred.await())
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
            val completableDeferred = CompletableDeferred<Boolean>()
            CoroutineScope(IO).launch {
                try {
                    //Arrange
                    val note = Note(
                        title = "Grown",
                        description = "dfgdfg",
                        time = "20:48",
                        date = "2024/04/01"
                    )
                    //Act
                    dao.upsertNote(note)
                    dao.deleteAllNotes()
                    dao.noteList().collect {
                        completableDeferred.complete(it.isEmpty())
                    }
                } catch (e: Exception) {
                    Assert.fail(e.message)
                }
            }
            //Assert
            Assert.assertTrue(completableDeferred.await())
        }
    }
}