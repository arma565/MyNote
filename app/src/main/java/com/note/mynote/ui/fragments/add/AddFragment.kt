package com.note.mynote.ui.fragments.add

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import com.note.mynote.R
import com.note.mynote.databinding.FragmentAddBinding
import com.note.mynote.db.viewmodel.NoteViewModel
import com.note.mynote.models.GlobalFunctions
import com.note.mynote.models.IViewNoteResponse
import com.note.mynote.models.Note
import dagger.hilt.android.AndroidEntryPoint
import org.joda.time.DateTime

/**
 * Add note
 */
@AndroidEntryPoint
class AddFragment : Fragment(), IViewNoteResponse {
    private lateinit var binding: FragmentAddBinding
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
        binding = FragmentAddBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dt = DateTime()
        val time = "${dt.hourOfDay}:${dt.minuteOfHour}"
        val date = "${dt.year}/${dt.monthOfYear}/${dt.dayOfMonth}"

        val mainActivity: AppCompatActivity = requireActivity() as AppCompatActivity
        mainActivity.setSupportActionBar(binding.toolbarAddFragment)

        (mainActivity as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menu.clear()
                menuInflater.inflate(R.menu.add_menu, menu)

                menu.findItem(R.id.item_save).setOnMenuItemClickListener {
                    val title: String = binding.edtTitle.text.toString()
                    val description: String = binding.edtDescription.text.toString()
                    val note = Note(title, description, time, date)
                    if (GlobalFunctions.validateNote(note, this@AddFragment)) {
                        noteViewModel.insertNote(note).observe(owner) { res ->
                            GlobalFunctions.getResult(requireActivity() as AppCompatActivity, res)
                        }
                    }
                    true
                }

                menu.findItem(R.id.item_clearAll).setOnMenuItemClickListener {
                    binding.edtTitle.setText("")
                    binding.edtDescription.setText("")
                    true
                }
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return false
            }
        })
    }

    override fun onEmptyTitle() {
        binding.edtTitle.error = getString(R.string.title_require)
    }

    override fun onEmptyDescription() {
        binding.edtTitle.error = getString(R.string.description_require)
    }

}