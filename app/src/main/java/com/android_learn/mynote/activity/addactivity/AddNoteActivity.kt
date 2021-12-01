package com.android_learn.mynote.activity.addactivity

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import com.android_learn.mynote.R
import com.android_learn.mynote.databinding.ActivityAddNoteBinding
import com.android_learn.mynote.models.Note
import com.android_learn.mynote.models.Type
import com.google.android.material.snackbar.Snackbar
import java.util.*

class AddNoteActivity : AppCompatActivity(), IViewNote {
    lateinit var binding: ActivityAddNoteBinding
    lateinit var notePresenter: NotePresenter
    lateinit var calender: Calendar
    private var mYear: Int = 0
    private var mMonth: Int = 0
    private var mDay: Int = 0
    private var mHour: Int = 0
    private var mMin: Int = 0
    var time: String = ""
    var date: String = ""
    lateinit var bundle: Bundle
    var type: Int = 0
    lateinit var note: Note
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        notePresenter = NotePresenter(this, this)
        bundle = intent.extras!!
        type = bundle.getInt("type")

        calender = Calendar.getInstance()
        mYear = calender[Calendar.YEAR]
        mMonth = calender[Calendar.MONTH]
        mDay = calender[Calendar.DAY_OF_MONTH]
        mHour = calender[Calendar.HOUR_OF_DAY]
        mMin = calender[Calendar.MINUTE]

        if(type == Type.ADD.type)
        {
            Toast.makeText(applicationContext , R.string.you_can_add_here , Toast.LENGTH_LONG).show()
        }else
        {
            Toast.makeText(applicationContext , R.string.you_can_edit_here , Toast.LENGTH_LONG).show()
            binding.txtAppTitle.text = getString(R.string.edit)
            binding.btnSave.text = getString(R.string.edit)
            note = bundle.getParcelable<Note>("myObj")!!
            binding.edtTitle.setText(note.title)
            binding.edtDescription.setText(note.description)
        }


        binding.imgBack.setOnClickListener{
            finish()
        }

        binding.imgTime.setOnClickListener {
            val timepicker: TimePickerDialog =
                TimePickerDialog(this,
                    { view, hourOfDay, minute -> time = "$hourOfDay:$minute" }, mHour, mMin, false)
            timepicker.show()
        }

        binding.imgDate.setOnClickListener {
            val datePicker: DatePickerDialog =
                DatePickerDialog(this,
                    { view: DatePicker?, year, month, dayOfMonth -> date = "$year/$month/$dayOfMonth" }, mYear, mMonth, mDay)
            datePicker.show()
        }



        binding.btnSave.setOnClickListener {
            val title : String = binding.edtTitle.text.toString()
            val description : String = binding.edtDescription.text.toString()

            if(type == Type.ADD.type)
            {
                val note: Note = Note(title, description, time, date)
                notePresenter.insertNote(note)
            }else
            {
                val noteEdit : Note = Note(note.id , title , description , time , date)
                notePresenter.updateNote(noteEdit)
            }
        }


    }

    override fun onSuccess() {
        Snackbar.make(binding.root, getString(R.string.success), Snackbar.LENGTH_LONG).show()
        finish()
    }

    override fun onFailure() {
        val snackBarFailure: Snackbar =
            Snackbar.make(binding.root, getString(R.string.unsuccessful), Snackbar.LENGTH_LONG)
                .setAction(getString(R.string.ok)) {
                }
        snackBarFailure.show()
    }

    override fun onEmptyTitle() {
        binding.edtTitle.error = getString(R.string.title_require)
    }

    override fun onEmptyDescription() {
        binding.edtDescription.error = getString(R.string.description_require)
    }

    override fun onEmptyTime() {
        val snackBarFailure: Snackbar =
            Snackbar.make(binding.root, getString(R.string.time_require), Snackbar.LENGTH_LONG)
                .setAction(getString(R.string.ok)) {

                }
        snackBarFailure.show()
    }

    override fun onEmptyDate() {
        val snackBarFailure: Snackbar =
            Snackbar.make(binding.root, getString(R.string.date_require), Snackbar.LENGTH_LONG)
                .setAction(getString(R.string.ok)) {
                }
        snackBarFailure.show()
    }
}
