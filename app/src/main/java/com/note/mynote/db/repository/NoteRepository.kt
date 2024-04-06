package com.note.mynote.db.repository

import com.note.mynote.db.NoteDao
import com.note.mynote.models.Note
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Repository
 * @param dao instance of Note Dao
 */
class NoteRepository @Inject constructor(
    private val dao: NoteDao
) {

    /**
     * Insert note
     * @param note instance of Note model
     */
    suspend fun insertNote(note: Note) = dao.insertNote(note)

    /**
     * Get note list
     */
    fun noteList(): Flow<List<Note>> = dao.noteList()

    /**
     * Update note
     * @param note instance of Note model
     */
    suspend fun updateNote(note: Note) = dao.updateNote(note)

    /**
     * Delete note
     * @param note instance of Note model
     */
    suspend fun delete(note: Note) = dao.deleteNote(note)

    /**
     * Delete all notes
     */
    suspend fun deleteAll() = dao.deleteAllNotes()
}