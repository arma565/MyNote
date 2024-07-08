package com.note.mynote.data.repository

import com.note.mynote.data.local.NoteDao
import com.note.mynote.data.models.Note
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Repository
 * @param dao instance of Note Dao
 */
open class NoteRepository @Inject constructor(
    private val dao: NoteDao
) : NoteDao {

    override suspend fun upsertNote(note: Note) = dao.upsertNote(note)

    override fun noteList(): Flow<List<Note>> = dao.noteList()

    override suspend fun deleteNote(note: Note) = dao.deleteNote(note)

    override suspend fun deleteAllNotes() = dao.deleteAllNotes()
}