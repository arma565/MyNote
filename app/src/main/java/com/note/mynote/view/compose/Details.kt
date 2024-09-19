package com.note.mynote.view.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.note.mynote.R
import com.note.mynote.data.models.Note
import com.note.mynote.viewmodel.RemoteNoteViewModel

/**
 * Details
 * Details of a note
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsFragmentComposeView(
    remoteNoteViewModel: RemoteNoteViewModel,
    note: Note,
    onEditClick: (updatedNoteId: Int) -> Unit,
    onShareClick: () -> Unit,
    onHomeClick: () -> Unit,
) {
    var showDialog by rememberSaveable { mutableStateOf(false) }

    if (showDialog) {
        DeleteDialogDetails(note, remoteNoteViewModel, onHomeClick = { onHomeClick() }) {
            showDialog = false
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = note.title!!)
                },
                actions = {
                    IconButton(onClick = {
                        showDialog = true
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = stringResource(id = R.string.delete)
                        )
                    }

                    IconButton(onClick = {
                        onEditClick(note.id)
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = stringResource(id = R.string.edit)
                        )
                    }

                    IconButton(onClick = {
                        onShareClick()
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Share,
                            contentDescription = stringResource(id = R.string.share)
                        )
                    }
                }
            )
        }) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
        ) {
            Text(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(all = 10.dp), text = note.description!!
            )
        }
    }

}

@Composable
private fun DeleteDialogDetails(
    note: Note,
    remoteNoteViewModel: RemoteNoteViewModel,
    onHomeClick: () -> Unit,
    onDismissRequest: () -> Unit
) {
    AlertDialog(icon = {
        Icon(
            Icons.Filled.Info,
            contentDescription = stringResource(id = R.string.delete)
        )
    },
        title = {
            Text(text = stringResource(id = R.string.delete))
        },
        text = { Text(text = stringResource(id = R.string.are_you_sure)) },
        onDismissRequest = {},
        confirmButton = {
            TextButton(onClick = {
                remoteNoteViewModel.deleteNote(note.id)
                onDismissRequest()
                onHomeClick()
            }) {
                Text(text = stringResource(id = R.string.confirm))
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismissRequest() }) {
                Text(text = stringResource(id = R.string.dismiss))
            }
        })
}