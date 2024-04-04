package com.note.mynote.db.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.note.mynote.db.repository.NoteRepository
import com.note.mynote.models.Note
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class NoteViewModel @Inject constructor(
    private val repository: NoteRepository
) : ViewModel() {

    private lateinit var insertNoteViewModelLiveData: MutableLiveData<Long>
    private lateinit var updateNoteViewModelLiveData: MutableLiveData<Int>
    private lateinit var deleteNoteViewModelLiveData: MutableLiveData<Int>

    /**
     * Insert method
     * @param note: Object of note model
     * This method will insert a note into database
     */
    fun insertNote(note: Note): LiveData<Long> {
        insertNoteViewModelLiveData = MutableLiveData()
        viewModelScope.launch {
            insertNoteViewModelLiveData.postValue(repository.insertNote(note))
        }
        return insertNoteViewModelLiveData
    }

    /**
     * Get note list
     * This method will get note list from database
     */
    fun getNoteList() = flow {
        this.emit(repository.noteList())
        delay(10000)
    }

    /**
     * Update method
     * @param note: Object of note model
     * This method will update a note from database
     */
    fun updateNote(note: Note): LiveData<Int> {
        updateNoteViewModelLiveData = MutableLiveData()
        viewModelScope.launch {
            updateNoteViewModelLiveData.postValue(repository.updateNote(note))
        }
        return updateNoteViewModelLiveData
    }

    /**
     * Delete method
     * @param note: Object of note model
     * This method will delete a note from database
     */
    fun deleteNote(note: Note): LiveData<Int> {
        deleteNoteViewModelLiveData = MutableLiveData()
        viewModelScope.launch {
            deleteNoteViewModelLiveData.postValue(repository.delete(note))
        }
        return deleteNoteViewModelLiveData
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
}