package com.note.mynote.view.fragments.add

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
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
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import com.note.mynote.R
import com.note.mynote.data.models.GlobalFunctions
import com.note.mynote.data.models.IViewNoteResponse
import com.note.mynote.data.models.Note
import com.note.mynote.viewmodel.NoteViewModel
import dagger.hilt.android.AndroidEntryPoint
import org.joda.time.DateTime

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

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AddFragmentComposeView(onSave: (note: Note) -> Unit) {
    var titleInput by rememberSaveable { mutableStateOf("") }
    var descriptionInput by rememberSaveable { mutableStateOf("") }

    var titleError by rememberSaveable { mutableStateOf(false) }
    var descriptionError by rememberSaveable { mutableStateOf(false) }

    val dt = DateTime()
    val time = "${dt.hourOfDay}:${dt.minuteOfHour}"
    val date = "${dt.year}/${dt.monthOfYear}/${dt.dayOfMonth}"

    Column {
        ConstraintLayout {
            val (topAppBarConst, inputConst) = createRefs()

            Scaffold(topBar = {
                TopAppBar(modifier = Modifier.constrainAs(topAppBarConst) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
                    title = {
                        Text(
                            stringResource(R.string.add),
                            fontWeight = FontWeight.Bold
                        )
                    },
                    actions = {
                        IconButton(onClick = {
                            val note = Note(
                                title = titleInput,
                                description = descriptionInput,
                                time = time,
                                date = date
                            )
                            if (GlobalFunctions.validateNote(note, object : IViewNoteResponse {
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
                                onSave(note)
                            }

                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_save_24),
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
                                painter = painterResource(id = R.drawable.baseline_clear_all_24),
                                contentDescription = stringResource(
                                    id = R.string.clear
                                )
                            )
                        }
                    }
                )
            }) { innerPadding ->
                Column(
                    modifier = Modifier.padding(innerPadding),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {}
            }

            ConstraintLayout(modifier = Modifier.constrainAs(inputConst) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }) {
                val (titleInputConst, descriptionInputConst) = createRefs()

                OutlinedTextField(
                    modifier = Modifier.constrainAs(titleInputConst) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                    value = titleInput,
                    onValueChange = { titleInput = it },
                    isError = titleError,
                    supportingText = {
                        if (titleError) {
                            Text(
                                text = stringResource(id = R.string.title_require),
                                modifier = Modifier.fillMaxWidth(),
                                color = MaterialTheme.colorScheme.error
                            )
                        } else {
                            Text(text = "")
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
                    value = descriptionInput,
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