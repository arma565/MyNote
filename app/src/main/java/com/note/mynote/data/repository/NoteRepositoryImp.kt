package com.note.mynote.data.repository

import com.note.mynote.data.local.NoteDao
import com.note.mynote.data.models.Note
import kotlinx.coroutines.flow.Flow

/**
 * Repository
 * @param dao instance of Note Dao
 */
class NoteRepositoryImp(
    private val dao: NoteDao
) : IRepository {

    override suspend fun upsertNote(note: Note) = dao.upsertNote(note)

    override suspend fun noteList(): Flow<List<Note>> = dao.noteList()

    override suspend fun delete(note: Note) = dao.deleteNote(note)

    override suspend fun deleteAll() = dao.deleteAllNotes()
}