package com.note.mynote.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.note.mynote.data.models.Note
import com.note.mynote.data.repository.RemoteNoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
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


    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    @OptIn(FlowPreview::class)
    val noteItems = searchText.debounce(1000L).onEach { _isSearching.update { true } }
        .combine(_getNoteListStateFlow) { text, noteItems ->
            if (text.isBlank()) {
                noteItems
            } else {
                delay(2000L)
                noteItems.filter {
                    it.doesMatchSearchQuery(text)
                }
            }

        }.onEach { _isSearching.update { false } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), _getNoteListStateFlow.value)

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

    fun onSearchTextChanged(text: String) {
        _searchText.value = text
    }
}