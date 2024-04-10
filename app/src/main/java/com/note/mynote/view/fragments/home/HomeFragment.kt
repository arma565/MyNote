package com.note.mynote.ui.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.note.mynote.R
import com.note.mynote.databinding.FragmentHomeBinding
import com.note.mynote.data.local.db.viewmodel.NoteViewModel
import com.note.mynote.data.local.db.models.GlobalFunctions
import com.note.mynote.data.local.db.models.Note
import com.note.mynote.ui.fragments.home.adapter.NoteAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val noteViewModel: NoteViewModel by viewModels()
    private lateinit var filter: List<Note>
    private lateinit var noteAdapter: NoteAdapter
    private lateinit var navController: NavController
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = GlobalFunctions.getNavControllerFragmentNote(requireActivity())

        binding.toolbarHome.title = getString(R.string.app_name)
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbarHome)
        (activity as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menu.clear()
                menuInflater.inflate(R.menu.home_menu, menu)
                val searchItem: MenuItem = menu.findItem(R.id.item_search)
                val clearAll: MenuItem = menu.findItem(R.id.item_clearAll)

                val searchView: SearchView = searchItem.actionView as SearchView
                searchView.queryHint = getString(R.string.search_here)
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        return false
                    }

                    override fun onQueryTextChange(searchedText: String?): Boolean {
                        if (searchedText.isNullOrBlank()) {
                            setList()
                            return false
                        }
                        val filteredList = filter.filter { note ->
                            note.title?.lowercase(Locale.ROOT)?.contains(searchedText)!!
                        }
                        if (filteredList.isEmpty()) {
                            Toast.makeText(context,getString(R.string.no_data_found),Toast.LENGTH_SHORT).show()
                        }
                        noteAdapter.setFilteredList(filteredList)
                        binding.recHome.setHasFixedSize(true)
                        setAdapter()
                        return true
                    }

                })

                clearAll.setOnMenuItemClickListener {
                    val alert = AlertDialog.Builder(requireContext())
                    alert.setTitle(R.string.delete)
                    alert.setMessage(getString(R.string.are_you_sure))
                    alert.setPositiveButton(getString(R.string.yes)) { _, _ ->
                        noteAdapter.clearAll()
                        noteAdapter = NoteAdapter(requireActivity(),noteViewModel , listOf())
                        setAdapter()
                    }
                    alert.setNegativeButton(getString(R.string.no)) { _, _ -> }
                    alert.show()
                    true
                }
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return false
            }

        })

        setList()

        binding.btnAdd.setOnClickListener {
            navController.navigate(R.id.action_homeFragment_to_addFragment)
        }
    }

    private fun setList() {
        CoroutineScope(Dispatchers.IO).launch {
            noteViewModel.getNoteList().collect { noteList ->
                filter = noteList
                CoroutineScope(Dispatchers.Main).launch {
                    noteAdapter = NoteAdapter(requireActivity(),noteViewModel, noteList)
                    setAdapter()
                }
            }
        }
    }

    private fun setAdapter() {
        binding.recHome.adapter = noteAdapter
        binding.recHome.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }
}