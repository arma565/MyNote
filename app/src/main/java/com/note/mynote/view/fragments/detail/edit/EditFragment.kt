package com.note.mynote.view.fragments.detail.edit

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTimeFilled
import androidx.compose.material.icons.filled.ClearAll
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.fragment.app.FragmentManager
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.note.mynote.R
import com.note.mynote.data.models.GlobalFunctions
import com.note.mynote.data.models.IViewNoteResponse
import com.note.mynote.data.models.Note
import org.joda.time.DateTime


/**
 * Edit note
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditFragmentComposeView(
    context: Context,
    parentFragmentManager: FragmentManager,
    note: Note,
    onSave: (note: Note) -> Unit
) {
    var titleInput by rememberSaveable { mutableStateOf(note.title) }
    var descriptionInput by rememberSaveable { mutableStateOf(note.description) }

    var titleError by rememberSaveable { mutableStateOf(false) }
    var descriptionError by rememberSaveable { mutableStateOf(false) }

    val dt = DateTime()
    var time: String = note.time!!
    var date: String = note.date!!

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(text = stringResource(id = R.string.edit))
            }, actions = {
                IconButton(onClick = {
                    val mHour = dt.hourOfDay
                    val mMin = dt.minuteOfHour
                    val materialTimePicker: MaterialTimePicker =
                        MaterialTimePicker.Builder()
                            .setTimeFormat(TimeFormat.CLOCK_12H)
                            .setTimeFormat(TimeFormat.CLOCK_24H)
                            .setHour(mHour)
                            .setMinute(mMin)
                            .setTitleText(context.getString(R.string.set_time))
                            .setPositiveButtonText(context.getString(R.string.ok))
                            .build()
                    materialTimePicker.addOnPositiveButtonClickListener {
                        time = "${materialTimePicker.hour}:${materialTimePicker.minute}"
                    }
                    materialTimePicker.show(
                        parentFragmentManager,
                        context.getString(R.string.time)
                    )

                }) {
                    Icon(
                        imageVector = Icons.Filled.AccessTimeFilled,
                        contentDescription = stringResource(
                            id = R.string.time
                        )
                    )
                }
                IconButton(onClick = {
                    val materialDatePicker = MaterialDatePicker.Builder.datePicker()
                        .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                        .setTitleText(context.getString(R.string.date))
                        .setPositiveButtonText("Ok")
                        .build()
                    materialDatePicker.addOnPositiveButtonClickListener { newDt ->
                        val newDate = DateTime(newDt)
                        date =
                            "${newDate.year}/${newDate.monthOfYear}/${newDate.dayOfMonth}"
                    }
                    materialDatePicker.show(
                        parentFragmentManager,
                        context.getString(R.string.date)
                    )
                }) {
                    Icon(
                        imageVector = Icons.Filled.DateRange,
                        contentDescription = stringResource(
                            id = R.string.date
                        )
                    )
                }
                IconButton(onClick = {
                    val updatedNote = Note(
                        note.id,
                        titleInput,
                        descriptionInput,
                        time,
                        date
                    )
                    if (GlobalFunctions.validateNote(updatedNote, object : IViewNoteResponse {
                            override fun onEmptyTitle() {
                                titleError = true
                                descriptionError = false
                            }

                            override fun onEmptyDescription() {
                                descriptionError = true
                                titleError = false
                            }

                            override fun onEmptyTitleAndDescription() {
                                titleError = true
                                descriptionError = true
                            }

                        })) {
                        onSave(updatedNote)
                    }

                }) {
                    Icon(
                        imageVector = Icons.Filled.Save,
                        contentDescription = stringResource(
                            id = R.string.save_data
                        )
                    )
                }

                IconButton(onClick = {
                    titleInput = ""
                    descriptionInput = ""
                    titleError = false
                    descriptionError = false
                }) {
                    Icon(
                        imageVector = Icons.Filled.ClearAll,
                        contentDescription = stringResource(
                            id = R.string.clear
                        )
                    )
                }
            })
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            ConstraintLayout(modifier = Modifier.fillMaxSize()) {
                val (inputConst) = createRefs()
                ConstraintLayout(modifier = Modifier
                    .constrainAs(inputConst) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .fillMaxWidth()
                    .wrapContentHeight()) {
                    val (titleInputConst, descriptionInputConst) = createRefs()
                    OutlinedTextField(
                        modifier = Modifier.constrainAs(titleInputConst) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        },
                        value = titleInput!!,
                        onValueChange = { titleInput = it },
                        isError = titleError,
                        supportingText = {
                            if (titleError) {
                                Text(
                                    text = stringResource(id = R.string.title_require),
                                    modifier = Modifier.fillMaxWidth(),
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        },
                        trailingIcon = {
                            if (titleError) {
                                Icon(
                                    Icons.Filled.Info,
                                    stringResource(id = R.string.title_require),
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        },
                        label = { Text(text = stringResource(id = R.string.title)) })

                    OutlinedTextField(
                        modifier = Modifier.constrainAs(descriptionInputConst) {
                            top.linkTo(titleInputConst.bottom)
                            bottom.linkTo(parent.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        },
                        value = descriptionInput!!,
                        onValueChange = { descriptionInput = it },
                        isError = descriptionError,
                        supportingText = {
                            if (descriptionError) {
                                Text(
                                    text = stringResource(id = R.string.description_require),
                                    modifier = Modifier.fillMaxWidth(),
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        }, trailingIcon = {
                            if (descriptionError) {
                                Icon(
                                    Icons.Filled.Info,
                                    stringResource(id = R.string.description_require),
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        },
                        label = { Text(text = stringResource(id = R.string.description)) })
                }
            }
        }
    }
}