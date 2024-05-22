package com.note.mynote.view.fragments.add

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import com.note.mynote.data.models.GlobalFunctions
import com.note.mynote.view.AddFragmentComposeView
import com.note.mynote.viewmodel.NoteViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * Add note
 */
@AndroidEntryPoint
class AddFragment : Fragment() {
    private lateinit var addFragmentComposeView: ComposeView
    private val noteViewModel: NoteViewModel by viewModels()
    private lateinit var owner: LifecycleOwner

    override fun onAttach(context: Context) {
        super.onAttach(context)
        owner = this
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).also {
            addFragmentComposeView = it
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addFragmentComposeView.setContent {
            AddFragmentComposeView(onSave = {
                noteViewModel.upsertNote(it)
                GlobalFunctions.getResult(requireActivity() as AppCompatActivity, 1)
            })
        }
    }
}