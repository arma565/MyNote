package com.note.mynote.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.android_learn.mynote.models.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note): Long

    @Update
    suspend fun updateNote(note: Note): Int

    @Query("select * from tbl_note")
    fun noteList(): Flow<List<Note>>

    @Query("select * from tbl_note where title like  '%'|| :search ||'%' ")
    fun searchNote(search: String): Flow<List<Note>>

    @Query("DELETE FROM tbl_note")
    suspend fun deleteAllNotes()

    @Delete
    suspend fun deleteNote(note: Note)
}