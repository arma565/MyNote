package com.note.mynote.view.compose

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ClearAll
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.note.mynote.R
import com.note.mynote.data.models.Note
import com.note.mynote.viewmodel.NoteViewModel
import java.util.Locale

/**
 * Home
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeFragmentComposeView(
    noteViewModel: NoteViewModel,
    onAddClick: () -> Unit,
    onDetailsClick: (id: Int) -> Unit
) {

    var showDialog by rememberSaveable { mutableStateOf(false) }

    if (showDialog) {
        DeleteDialog(noteViewModel, onDismissRequest = {
            showDialog = false
        })
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.app_name)) },
                actions = {
                    IconButton(onClick = {
                        showDialog = true
                    }) {
                        Icon(
                            imageVector = Icons.Default.ClearAll,
                            contentDescription = stringResource(
                                id = R.string.clear
                            )
                        )
                    }

                    IconButton(onClick = {
                        onAddClick()
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = stringResource(
                                id = R.string.add
                            )
                        )
                    }
                })
        }

    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            var text by remember { mutableStateOf("") }
            var active by remember { mutableStateOf(false) }
            val focusManager = LocalFocusManager.current
            val historyList: MutableList<Note> = remember { mutableStateListOf() }

            ConstraintLayout {

                val (searchBar, lazyColumn, addButton) = createRefs()

                SearchBar(
                    modifier = Modifier
                        .constrainAs(searchBar) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                        .fillMaxWidth(),
                    colors = SearchBarDefaults.colors(
                        containerColor = colorResource(id = R.color.white),
                        dividerColor = colorResource(
                            id = R.color.black
                        )
                    ),
                    query = text,
                    onQueryChange = {
                        text = it
                    },
                    onSearch = {
                        if (it.isNotEmpty()) {
                            historyList.add(Note(title = it))
                        }
                        focusManager.clearFocus()
                    },
                    active = active,
                    onActiveChange = {
                        active = it
                    },
                    placeholder = { Text(text = stringResource(id = R.string.search_here)) },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Search, contentDescription = "Search Icon")
                    }, trailingIcon = {
                        if (active) Icon(modifier = Modifier.clickable {
                            if (text.isEmpty()) {
                                active = false
                                return@clickable
                            }
                            text = ""
                        }, imageVector = Icons.Default.Clear, contentDescription = "Close Icon")
                    }
                ) {
                    val filterList: List<Note> =
                        (noteViewModel.getNoteList.collectAsState().value).filter {
                            it.title!!.lowercase(Locale.ROOT).contains(text)
                        }
                    if (filterList.isEmpty()) {
                        Toast.makeText(
                            LocalContext.current,
                            stringResource(R.string.no_data_found),
                            Toast.LENGTH_SHORT
                        ).show()
                        return@SearchBar
                    }
                    if (text.isEmpty()) {
                        historyList.forEach {
                            Row(modifier = Modifier
                                .padding(all = 14.dp)
                                .clickable { text = it.title!! }) {
                                Icon(
                                    modifier = Modifier.padding(end = 10.dp),
                                    imageVector = Icons.Default.History,
                                    contentDescription = "History Icon",
                                )
                                Text(text = it.title!!)
                            }
                        }
                        return@SearchBar
                    }
                    //SearchBar
                    LazyColumn {
                        items(filterList) { filterNote ->
                            ElevatedCard(modifier = Modifier
                                .padding(10.dp)
                                .fillMaxWidth()
                                .height(50.dp),
                                colors = CardColors(
                                    containerColor = colorResource(id = R.color.white),
                                    contentColor = colorResource(
                                        id = R.color.black
                                    ),
                                    disabledContentColor = Color.Gray,
                                    disabledContainerColor = Color.Gray
                                ),
                                elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
                                onClick = {
                                    onDetailsClick(filterNote.id)
                                }) {
                                NoteLazyColumn(filterNote)
                            }
                        }
                    }
                }

                //Home
                val noteList: List<Note> =
                    noteViewModel.getNoteList.collectAsState().value
                LazyColumn(
                    modifier = Modifier
                        .constrainAs(lazyColumn) {
                            top.linkTo(searchBar.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            bottom.linkTo(addButton.top)
                        }
                        .fillMaxWidth()
                ) {
                    items(noteList) { note ->
                        ElevatedCard(modifier = Modifier
                            .padding(10.dp)
                            .fillMaxWidth()
                            .height(50.dp),
                            colors = CardColors(
                                containerColor = colorResource(id = R.color.white),
                                contentColor = colorResource(
                                    id = R.color.black
                                ),
                                disabledContentColor = Color.Gray,
                                disabledContainerColor = Color.Gray
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
                            onClick = {
                                onDetailsClick(note.id)
                            }) {
                            NoteLazyColumn(note)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun NoteLazyColumn(note: Note) {
    Row {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(5.dp)
        ) {
            val (titleConst, dateTimeConst) = createRefs()
            Text(modifier = Modifier.constrainAs(titleConst) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                bottom.linkTo(parent.bottom)
            }, text = note.title!!)
            Row(modifier = Modifier.constrainAs(dateTimeConst) {
                top.linkTo(parent.top)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
            }) {
                Text(
                    modifier = Modifier.padding(end = 5.dp),
                    text = note.date!!
                )
                Text(text = note.time!!)
            }
        }
    }
}

@Composable
private fun DeleteDialog(noteViewModel: NoteViewModel, onDismissRequest: () -> Unit) {
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
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(onClick = {
                noteViewModel.deleteNotes()
                onDismissRequest()
            }) {
                Text(text = stringResource(id = R.string.confirm))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(text = stringResource(id = R.string.dismiss))
            }
        })
}