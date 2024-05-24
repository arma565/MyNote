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
import com.note.mynote.viewmodel.NoteViewModel

/**
 * Details
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsFragmentComposeView(
    noteViewModel: NoteViewModel,
    note: Note,
    onEditClick: (updatedNoteId: Int) -> Unit,
    onShareClick: () -> Unit,
    onHomeClick: () -> Unit,
) {
    var showDialog by rememberSaveable { mutableStateOf(false) }
    if (showDialog) {
        DeleteDialogDetails(note, noteViewModel, onHomeClick = { onHomeClick() }) {
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
                        Icon(imageVector = Icons.Filled.Delete, contentDescription = "Delete")
                    }

                    IconButton(onClick = {
                        onEditClick(note.id)
                    }) {
                        Icon(imageVector = Icons.Filled.Edit, contentDescription = "Edit")
                    }

                    IconButton(onClick = {
                        onShareClick()
                    }) {
                        Icon(imageVector = Icons.Filled.Share, contentDescription = "Share")
                    }
                }
            )
        }) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
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
    noteViewModel: NoteViewModel,
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
        onDismissRequest = { onDismissRequest() },
        confirmButton = {
            TextButton(onClick = {
                noteViewModel.deleteNote(note)
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