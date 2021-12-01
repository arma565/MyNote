package com.android_learn.mynote.activity.addactivity

import android.content.Context
import com.android_learn.mynote.db.AppDatabase
import com.android_learn.mynote.models.Note

class NoteInteract(context: Context) {
    var context: Context = context

    var appDatabase : AppDatabase = AppDatabase.getInstance(context)

    fun insertNote(note : Note , iView : IViewNote)
    {
        if(note.title!!.isEmpty())
        {
            iView.onEmptyTitle()
            return
        }
        if(note.description!!.isEmpty())
        {
            iView.onEmptyDescription()
            return
        }
        if(note.time!!.isEmpty())
        {
            iView.onEmptyTime()
            return
        }
        if(note.date!!.isEmpty())
        {
            iView.onEmptyDate()
            return
        }

        val res : Long = appDatabase.noteDao().insertNote(note)
        if(res > 0)
        {
            iView.onSuccess()
        }else
        {
            iView.onFailure()
        }
    }

    fun updateNote(note : Note , iView : IViewNote)
    {
        if(note.title!!.isEmpty())
        {
            iView.onEmptyTitle()
            return
        }
        if(note.description!!.isEmpty())
        {
            iView.onEmptyDescription()
            return
        }
        if(note.time!!.isEmpty())
        {
            iView.onEmptyTime()
            return
        }
        if(note.date!!.isEmpty())
        {
            iView.onEmptyDate()
            return
        }

        val res : Int = appDatabase.noteDao().updateNote(note)
        if(res > 0)
        {
            iView.onSuccess()
        }else
        {
            iView.onFailure()
        }
    }
}