package com.note.mynote.data.repository

import com.note.mynote.data.models.Note
import kotlinx.coroutines.flow.Flow

interface IRepository {
    /**
     * Insert note
     * @param note instance of Note model
     */
    suspend fun upsertNote(note: Note)

    /**
     * Get note list
     */
    suspend fun noteList(): Flow<List<Note>>

    /**
     * Delete note
     * @param note instance of Note model
     */
    suspend fun delete(note: Note)

    /**
     * Delete all notes
     */
    suspend fun deleteAll()
}