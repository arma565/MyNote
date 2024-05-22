package com.note.mynote.view.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.note.mynote.R
import com.note.mynote.data.models.GlobalFunctions
import com.note.mynote.view.HomeFragmentComposeView
import com.note.mynote.viewmodel.NoteViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var homeComposeView: ComposeView
    private val noteViewModel: NoteViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return ComposeView(requireContext()).also {
            homeComposeView = it
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navigation =  GlobalFunctions.getNavControllerFragmentNote(requireActivity())
        homeComposeView.setContent {
            HomeFragmentComposeView(noteViewModel , onAddClick = {
                    navigation.navigate(R.id.action_homeFragment_to_addFragment)
            }) {
                navigation.navigate(R.id.action_homeFragment_to_detailsFragment , bundleOf("note" to it))
            }
        }
    }
}