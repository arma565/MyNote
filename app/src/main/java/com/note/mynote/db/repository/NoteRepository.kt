package com.note.mynote.db.repository

import com.note.mynote.db.NoteDao
import com.note.mynote.models.Note
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class NoteRepository @Inject constructor(
    private val dao : NoteDao
) {
    suspend fun insertNote(note: Note) = dao.insertNote(note)

    fun noteList() : List<Note> = dao.noteList()

    suspend fun updateNote(note: Note) = dao.updateNote(note)

    suspend fun delete(note: Note) = dao.deleteNote(note)

    suspend fun deleteAll() = dao.deleteAllNotes()
}