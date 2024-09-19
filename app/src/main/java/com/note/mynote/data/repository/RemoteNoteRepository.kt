package com.note.mynote.data.repository

import com.note.mynote.data.models.Note
import com.note.mynote.data.remote.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class RemoteNoteRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun createNote(note: Note): Note = apiService.createNote(note).body()!!

    suspend fun getNotes(): Flow<List<Note>> = flowOf(apiService.getNotes().body()!!)

    suspend fun getNote(id: Int): Flow<Note> = flowOf(apiService.getNote(id).body()!!)

    suspend fun updateNote(id: Int, updateNote: Note) = apiService.updateNote(id, updateNote)

    suspend fun deleteNote(id: Int) = apiService.deleteNote(id)

    suspend fun deleteAllNotes() = apiService.deleteAllNotes()
}