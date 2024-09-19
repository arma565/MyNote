package com.note.mynote.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.note.mynote.data.models.Note
import com.note.mynote.data.repository.RemoteNoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RemoteNoteViewModel @Inject constructor(
    private val remoteNoteRepository: RemoteNoteRepository
) : ViewModel() {

    private var _getNoteListStateFlow: MutableStateFlow<List<Note>> =
        MutableStateFlow(mutableListOf())
    var getNoteListStateFlow = _getNoteListStateFlow.asStateFlow()

    private var _getNoteStateFlow: MutableStateFlow<Note> = MutableStateFlow(Note())
    var getNoteStateFlow = _getNoteStateFlow.asStateFlow()

    fun createNote(note: Note) {
        viewModelScope.launch {
            remoteNoteRepository.createNote(note)
        }
    }

    fun getNotes() {
        viewModelScope.launch {
            remoteNoteRepository.getNotes().collect {
                _getNoteListStateFlow.emit(it)
                delay(1000)
            }
        }
    }

    fun getNote(id: Int) {
        viewModelScope.launch {
            remoteNoteRepository.getNote(id).collect {
                _getNoteStateFlow.emit(it)
                delay(1000)
            }
        }
    }

    fun updateNote(id: Int, note: Note) {
        viewModelScope.launch {
            remoteNoteRepository.updateNote(id, note)
        }
    }

    fun deleteNote(id: Int) {
        viewModelScope.launch {
            remoteNoteRepository.deleteNote(id)
        }
    }

    fun deleteAllNotes() {
        viewModelScope.launch {
            remoteNoteRepository.deleteAllNotes()
        }
    }
}