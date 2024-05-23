package com.note.mynote.viewmodel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.note.mynote.data.models.Note
import com.note.mynote.data.repository.IRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * View model
 * @param repository instance of NoteRepositoryImp
 */
@HiltViewModel
class NoteViewModel @Inject constructor(
    private val repository: IRepository
) : ViewModel() {

    var getNote : Note = Note()

    private var _getNoteListStateFlow: MutableStateFlow<List<Note>> =
        MutableStateFlow(mutableListOf())
    var getNoteList = _getNoteListStateFlow.asStateFlow()

    init {
        prepareNoteList()
    }

    /**
     * Upsert method
     * @param note: Object of note model
     * This method will insert or update a note
     */
    fun upsertNote(note: Note) {
        viewModelScope.launch {
            repository.upsertNote(note)
        }
    }

    /**
     * Get note list
     * This method will get note list from database
     */
    private fun prepareNoteList() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.noteList().collect {
                _getNoteListStateFlow.emit(it)
            }
        }
    }

    /**
     * Delete method
     * @param note: Object of note model
     * This method will delete a note from database
     */
    fun deleteNote(note: Note) {
        viewModelScope.launch {
            repository.delete(note)
        }
    }

    /**
     * Delete all method
     * This method will delete all notes from database
     */
    fun deleteNotes() {
        viewModelScope.launch {
            repository.deleteAll()
        }
    }

    fun getSpecificNote(id: Int) {
        viewModelScope.launch {
            repository.noteList().collect { noteList ->
                if (noteList.any { it.id == id }){
                    getNote = noteList.first { it.id == id }
                }
            }
        }
    }
}