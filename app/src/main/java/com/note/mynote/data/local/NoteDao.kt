package com.note.mynote.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.note.mynote.data.models.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    /**
     * Insert or update note
     */
    @Upsert
    suspend fun upsertNote(note: Note)

    /**
     * Get note list
     */
    @Query("SELECT * FROM tbl_note ORDER BY id DESC")
    fun noteList(): Flow<List<Note>>

    /**
     * Delete note
     */
    @Delete
    suspend fun deleteNote(note: Note)

    /**
     * Delete all notes
     */
    @Query("DELETE FROM tbl_note")
    suspend fun deleteAllNotes()
}