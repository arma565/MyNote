package com.note.mynote.view.fragments.home.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.note.mynote.R
import com.note.mynote.databinding.NoteRowBinding
import com.note.mynote.viewmodel.NoteViewModel
import com.note.mynote.data.models.GlobalFunctions
import com.note.mynote.data.models.Note

/**
 * recycler adapter
 */
class NoteAdapter(
    private val activity : FragmentActivity,
    private var noteViewModel: NoteViewModel,
    private var noteList: List<Note>
) : RecyclerView.Adapter<NoteAdapter.NoteVH>() {

    private lateinit var binding: NoteRowBinding

    @SuppressLint("NotifyDataSetChanged")
    fun setFilteredList(noteList: List<Note>) {
        this.noteList = noteList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteVH {
        binding = DataBindingUtil.bind(
            LayoutInflater.from(parent.context).inflate(R.layout.note_row, null)
        )!!
        return NoteVH(binding)
    }

    class NoteVH(binding: NoteRowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: NoteVH, position: Int) {
        val note: Note = noteList[position]
        binding.note = note

        binding.cardNote.setOnClickListener {
            GlobalFunctions.getNavControllerFragmentNote(activity).navigate(R.id.action_homeFragment_to_detailsFragment,
                bundleOf("note" to note)
            )
        }
    }

    override fun getItemCount(): Int {
        return noteList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clearAll() {
        noteViewModel.deleteNotes()
        notifyDataSetChanged()
    }
}