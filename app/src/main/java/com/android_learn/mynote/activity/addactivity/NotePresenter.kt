package com.android_learn.mynote.activity.addactivity

import android.content.Context
import com.android_learn.mynote.models.Note

class NotePresenter(context: Context , iView : IViewNote) : IViewNote {
    var context: Context = context
    var iView : IViewNote = iView
    var noteInteract: NoteInteract = NoteInteract(context)

    fun insertNote(note : Note)
    {
        noteInteract.insertNote(note , this)
    }

    fun updateNote(note : Note)
    {
        noteInteract.updateNote(note , this)
    }

    override fun onSuccess() {
        iView.onSuccess()
    }

    override fun onFailure() {
        iView.onFailure()
    }

    override fun onEmptyTitle() {
       iView.onEmptyTitle()
    }

    override fun onEmptyDescription() {
       iView.onEmptyDescription()
    }

    override fun onEmptyTime() {
       iView.onEmptyTime()
    }

    override fun onEmptyDate() {
        iView.onEmptyDate()
    }


}