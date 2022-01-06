package com.android_learn.mynote.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android_learn.mynote.R
import com.android_learn.mynote.activity.addactivity.AddNoteActivity
import com.android_learn.mynote.db.AppDatabase
import com.android_learn.mynote.models.Note
import com.android_learn.mynote.models.Type

class NoteAdapter(context: Context, noteList: MutableList<Note>) : RecyclerView.Adapter<NoteVH>() {
    val context: Context = context
    private var appDatabase: AppDatabase = AppDatabase.getInstance(context)
    private var noteList: MutableList<Note> = noteList


    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteVH {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.note_row, null)
        return NoteVH(view)
    }

    override fun onBindViewHolder(holder: NoteVH, position: Int) {
        val note: Note = noteList[position]
        holder.txt_title.text = note.title
        holder.txt_description.text = note.description
        holder.txt_time.text = note.time
        holder.txt_date.text = note.date
        holder.img_delete.setOnClickListener {
            appDatabase.noteDao().deleteNote(note.id)
            noteList.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, noteList.size)
        }
        holder.img_edit.setOnClickListener{
             val intentEdit : Intent = Intent(context , AddNoteActivity::class.java)
            intentEdit.putExtra("type" , Type.EDIT.type)
            intentEdit.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intentEdit.putExtra("myObj" , note)
            context.startActivity(intentEdit)
        }
    }

    override fun getItemCount(): Int {
        return noteList.size
    }
}