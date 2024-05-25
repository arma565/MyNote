package com.note.mynote.view.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTimeFilled
import androidx.compose.material.icons.filled.ClearAll
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.constraintlayout.compose.ConstraintLayout
import com.note.mynote.R
import com.note.mynote.data.models.GlobalFunctions
import com.note.mynote.data.models.IViewNoteResponse
import com.note.mynote.data.models.Note
import org.joda.time.DateTime
import org.joda.time.DateTimeUtils

/**
 * Edit note
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditFragmentComposeView(
    note: Note,
    onSave: (note: Note) -> Unit
) {
    var time = ""
    var date = ""

    val dateT = DateTime()
    val timePickerState = rememberTimePickerState(dateT.hourOfDay, dateT.minuteOfHour, true)
    var showTimePicker by rememberSaveable { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(dateT.millis)
    var showDatePicker by remember { mutableStateOf(false) }


    var titleInput by rememberSaveable { mutableStateOf(note.title) }
    var descriptionInput by rememberSaveable { mutableStateOf(note.description) }

    var titleError by rememberSaveable { mutableStateOf(false) }
    var descriptionError by rememberSaveable { mutableStateOf(false) }



    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(text = stringResource(id = R.string.edit))
            }, actions = {
                IconButton(onClick = {
                    showTimePicker = true
                }) {
                    Icon(
                        imageVector = Icons.Filled.AccessTimeFilled,
                        contentDescription = stringResource(
                            id = R.string.time
                        )
                    )
                }
                IconButton(onClick = {
                    showDatePicker = true
                }) {
                    Icon(
                        imageVector = Icons.Filled.DateRange,
                        contentDescription = stringResource(
                            id = R.string.date
                        )
                    )
                }
                IconButton(onClick = {
                    if (time.isEmpty()) {
                        time = note.time!!
                    }
                    if (date.isEmpty()) {
                        date = note.date!!
                    }
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

            if (showTimePicker) {
                ShowDateTimeDialog(
                    onDismissRequest = {
                        showTimePicker = false
                    },
                    onConfirmButton = {
                        TextButton(onClick = {
                            time = "${timePickerState.hour}:${timePickerState.minute}"
                            showTimePicker = false
                        }) {
                            Text(text = "Confirm")
                        }
                    },
                    onDismissButton = {
                        TextButton(onClick = {
                            showTimePicker = false
                        }) {
                            Text(text = "Cancel")
                        }
                    }
                ) {
                    TimePicker(state = timePickerState)
                }
            }
            if (showDatePicker) {
                ShowDateTimeDialog(
                    onDismissRequest = {
                        showDatePicker = false
                    },
                    onConfirmButton = {
                        TextButton(onClick = {
                            DateTimeUtils.setCurrentMillisFixed(datePickerState.selectedDateMillis!!)
                            val dt = DateTime()
                            date = "${dt.year}/${dt.monthOfYear}/${dt.dayOfMonth}"
                            showDatePicker = false
                        }) {
                            Text(text = "Confirm")
                        }
                    },
                    onDismissButton = {
                        TextButton(onClick = {
                            showDatePicker = false
                        }) {
                            Text(text = "Cancel")
                        }
                    }
                ) {
                    DatePicker(state = datePickerState)
                }
            }

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
                            if (titleInput!!.isNotEmpty()) {
                                IconButton(onClick = {
                                    titleInput = ""
                                }) {
                                    Icon(
                                        imageVector = Icons.Filled.Close,
                                        contentDescription = "Close"
                                    )
                                }
                            }
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
                            if (descriptionInput!!.isNotEmpty()) {
                                IconButton(onClick = {
                                    descriptionInput = ""
                                }) {
                                    Icon(
                                        imageVector = Icons.Filled.Close,
                                        contentDescription = "Close"
                                    )
                                }
                            }
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

@Composable
private fun ShowDateTimeDialog(
    onDismissRequest: () -> Unit,
    onConfirmButton: @Composable () -> Unit,
    onDismissButton: @Composable (() -> Unit)? = null,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    content: @Composable () -> Unit
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            tonalElevation = 6.dp,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(
                    shape = MaterialTheme.shapes.extraLarge,
                    color = containerColor
                ), color = containerColor
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                content()
                Row(
                    modifier = Modifier
                        .height(40.dp)
                        .fillMaxWidth()
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    onDismissButton?.invoke()
                    onConfirmButton()
                }
            }
        }
    }
}