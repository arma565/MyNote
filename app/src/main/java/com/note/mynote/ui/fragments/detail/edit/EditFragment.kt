package com.note.mynote.ui.fragments.detail.edit

import android.content.Context
import android.os.Build
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
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.note.mynote.R
import com.note.mynote.databinding.FragmentEditBinding
import com.note.mynote.db.viewmodel.NoteViewModel
import com.note.mynote.models.GlobalFunctions
import com.note.mynote.models.IViewNoteResponse
import com.note.mynote.models.Note
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import org.joda.time.DateTime

/**
 * Edit note
 */
@Suppress("DEPRECATION")
@AndroidEntryPoint
class EditFragment : Fragment(),IViewNoteResponse {
    private lateinit var binding: FragmentEditBinding
    private lateinit var note: Note
    private var time: String = ""
    private var date : String = ""
    private val noteViewModel : NoteViewModel by viewModels()
    private lateinit var owner : LifecycleOwner

    override fun onAttach(context: Context) {
        super.onAttach(context)
        owner = this
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit, container, false)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            note = arguments?.getParcelable("note", Note::class.java)!!
        }
        note = arguments?.getParcelable("note")!!
        binding.note = note
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val myActivity: AppCompatActivity = requireActivity() as AppCompatActivity
        myActivity.setSupportActionBar(binding.toolbarEditFragment)

        (myActivity as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menu.clear()
                menuInflater.inflate(R.menu.note_edit, menu)

                val itemTime: MenuItem = menu.findItem(R.id.item_time)
                val itemDate: MenuItem = menu.findItem(R.id.item_date)
                val itemSave: MenuItem = menu.findItem(R.id.item_save)
                val itemClearAll: MenuItem = menu.findItem(R.id.item_clearAll)

                val dt = DateTime()

                itemTime.setOnMenuItemClickListener {
                    val mHour = dt.hourOfDay
                    val mMin = dt.minuteOfHour
                    val materialTimePicker : MaterialTimePicker = MaterialTimePicker.Builder()
                        .setTimeFormat(TimeFormat.CLOCK_12H)
                        .setTimeFormat(TimeFormat.CLOCK_24H)
                        .setHour(mHour)
                        .setMinute(mMin)
                        .setTitleText(getString(R.string.set_time))
                        .setPositiveButtonText(getString(R.string.ok))
                        .build()
                    materialTimePicker.addOnPositiveButtonClickListener {
                        time = "${materialTimePicker.hour}:${materialTimePicker.minute}"
                    }
                    materialTimePicker.show(parentFragmentManager,getString(R.string.time))
                    true
                }

                itemDate.setOnMenuItemClickListener {
                    val materialDatePicker = MaterialDatePicker.Builder.datePicker()
                        .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                        .setTitleText(getString(R.string.set_date))
                        .setPositiveButtonText(getString(R.string.ok))
                        .build()
                    materialDatePicker.addOnPositiveButtonClickListener {newDt->
                        val newDate = DateTime(newDt)
                        date = "${newDate.year}/${newDate.monthOfYear}/${newDate.dayOfMonth}"
                    }
                    materialDatePicker.show(parentFragmentManager,getString(R.string.date))
                    true
                }

                itemSave.setOnMenuItemClickListener {
                    val title = binding.edtTitle.text.toString()
                    val description = binding.edtDescription.text.toString()
                    val note = Note(note.id,title,description, time, date)
                    if (GlobalFunctions.validateNote(note,this@EditFragment)){
                        noteViewModel.updateNote(note).observe(owner){res->
                            GlobalFunctions.getResult(myActivity,res.toLong())
                        }
                    }
                    true
                }

                itemClearAll.setOnMenuItemClickListener {
                    binding.edtTitle.setText("")
                    binding.edtDescription.setText("")
                    true
                }

            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return false
            }

        })

        time = note.time!!
        date = note.date!!


    }

    override fun onEmptyTitle() {
        binding.edtTitle.error = getString(R.string.title_require)
    }

    override fun onEmptyDescription() {
        binding.edtDescription.error = getString(R.string.description_require)
    }
}