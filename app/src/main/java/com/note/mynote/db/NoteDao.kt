package com.note.mynote.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.note.mynote.models.Note

@Dao
interface NoteDao {

    /**
     * Insert note
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note): Long

    /**
     * Get note list
     */
    @Query("SELECT * FROM tbl_note")
    fun noteList(): List<Note>

    /**
     * Update note
     */
    @Update
    suspend fun updateNote(note: Note): Int

    /**
     * Delete note
     */
    @Delete
    suspend fun deleteNote(note: Note) : Int

    /**
     * Delete all notes
     */
    @Query("DELETE FROM tbl_note")
    suspend fun deleteAllNotes()
}