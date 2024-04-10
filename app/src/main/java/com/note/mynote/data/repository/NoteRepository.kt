package com.note.mynote.data.local.db.repository

import com.note.mynote.data.local.db.NoteDao
import com.note.mynote.data.local.db.models.Note
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