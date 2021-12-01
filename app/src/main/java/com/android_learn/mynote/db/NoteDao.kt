package com.android_learn.mynote.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.android_learn.mynote.models.Note

@Dao
interface NoteDao {

    @Insert
    fun insertNote(note : Note) : Long

    @Update
    fun updateNote(note : Note) : Int

    @Query("select * from tbl_note")
    fun noteList() : MutableList<Note>

    @Query("select * from tbl_note where title like  '%'|| :search ||'%' ")
    fun searchNote(search : String) : MutableList<Note>

    @Delete
    fun deleteAllNote(note : Note)

    @Query("delete from tbl_note where id = :id")
    fun deleteNote(id: Int)




}