package com.note.mynote.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.note.mynote.data.models.Note
import com.note.mynote.data.repository.IRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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

    /**
     * Get specific note using id
     * @param id note id
     * This method get a note using note id from database
     */
    fun getSpecificNote(id: Int) : Note {
        val get = getNoteList.value
        Log.d("getSize", "${get.size}")
        if (get.any { it.id == id }) {
            return get.first { it.id == id }
        }
        return Note()
    }
}