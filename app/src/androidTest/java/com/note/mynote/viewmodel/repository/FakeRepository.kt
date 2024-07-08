package com.note.mynote.viewmodel.repository

import com.note.mynote.data.local.NoteDao
import com.note.mynote.data.models.Note
import com.note.mynote.data.repository.NoteRepository
import kotlinx.coroutines.flow.Flow

class FakeRepository(private val dao : NoteDao) : NoteRepository(dao)  {
    override suspend fun upsertNote(note: Note) = dao.upsertNote(note)

    override  fun noteList(): Flow<List<Note>> = dao.noteList()

    override suspend fun deleteNote(note: Note) = dao.deleteNote(note)

    override suspend fun deleteAllNotes() = dao.deleteAllNotes()
}