package com.note.mynote.view.compose

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ClearAll
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.pullToRefresh
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.note.mynote.R
import com.note.mynote.data.models.GlobalFunctions.checkNetwork
import com.note.mynote.data.models.Note
import com.note.mynote.view.activity.ui.theme.Black
import com.note.mynote.view.activity.ui.theme.White
import com.note.mynote.viewmodel.RemoteNoteViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Home
 * Show all added notes
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeFragmentComposeView(
    activity: ComponentActivity,
    remoteNoteViewModel: RemoteNoteViewModel,
    onAddClick: () -> Unit,
    onDetailsClick: (id: Long) -> Unit,
    onDismissDialog: () -> Unit
) {
    var isConnected by rememberSaveable { mutableStateOf(false) }
    var itemCount by remember { mutableIntStateOf(15) }
    var isRefreshing by remember { mutableStateOf(false) }
    val state = rememberPullToRefreshState()
    val refreshCoroutineScope = rememberCoroutineScope()
    var showDialog by rememberSaveable { mutableStateOf(false) }
    var showSearchBar by rememberSaveable { mutableStateOf(false) }
    val searchText by remoteNoteViewModel.searchText.collectAsState()
    val noteItems by remoteNoteViewModel.noteItems.collectAsState()
    val isSearching by remoteNoteViewModel.isSearching.collectAsState()

    if (!isConnected) {
        checkNetwork(activity) { isNetworkConnected ->
            isConnected = isNetworkConnected
        }
        return
    }

    if (showDialog) {
        DeleteDialog(remoteNoteViewModel, onDismissRequest = {
            showDialog = false
            onDismissDialog()
        })
    }

    val onRefresh: () -> Unit = {
        isRefreshing = true
        refreshCoroutineScope.launch {
            delay(5000)
            itemCount += 5
            isRefreshing = false
        }
    }


    Scaffold(
        modifier = Modifier.pullToRefresh(
            isRefreshing = isRefreshing,
            state = state,
            onRefresh = onRefresh
        ),
        topBar = {
            TopAppBarInvoke(onRefresh = onRefresh, onShowSearchBar = {
                showSearchBar = true
            }, onShowDeleteDialog = {
                showDialog = true
            }) {
                onAddClick()
            }
        }

    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            remoteNoteViewModel.getNotes()
            val noteList = remoteNoteViewModel.getNoteListStateFlow.collectAsState().value
            if (!isRefreshing) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(10.dp)
                ) {
                    if (showSearchBar) {
                        SearchBarBox(
                            searchText,
                            remoteNoteViewModel,
                            isSearching,
                            noteItems,
                            onDetailsClick
                        ) {
                            showSearchBar = false
                        }
                    } else {
                        NoteLazyColumn(noteList = noteList) {
                            onDetailsClick(it)
                        }
                    }
                }
            }
            if (isRefreshing) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            } else {
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth(),
                    progress = { state.distanceFraction })
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarInvoke(
    onRefresh: () -> Unit,
    onShowSearchBar: () -> Unit,
    onShowDeleteDialog: () -> Unit,
    onAddClick: () -> Unit
) {
    TopAppBar(
        title = { Text(text = stringResource(id = R.string.app_name)) },
        actions = {
            IconButton(onClick = {
                onShowSearchBar()
            }) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = stringResource(
                        id = R.string.search_here
                    )
                )
            }

            IconButton(onClick = {
                onShowDeleteDialog()
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

            IconButton(onClick = onRefresh) {
                Icon(
                    imageVector = Icons.Filled.Refresh,
                    contentDescription = stringResource(R.string.trigger_refresh)
                )
            }
        })
}

@Composable
private fun NoteLazyColumn(noteList: List<Note>, onDetailsClick: (id: Long) -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        items(noteList) { note ->
            ElevatedCard(modifier = Modifier
                .offset(10.dp)
                .padding(10.dp)
                .fillMaxWidth()
                .height(50.dp),
                colors = CardColors(
                    containerColor = White,
                    contentColor = Black,
                    disabledContentColor = Color.Gray,
                    disabledContainerColor = Color.Gray
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
                onClick = {
                    onDetailsClick(note.id.toLong())
                }) {
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
    }
}

@Composable
private fun SearchBarBox(
    searchText: String,
    remoteNoteViewModel: RemoteNoteViewModel,
    isSearching: Boolean,
    noteItems: List<Note>,
    onDetailsClick: (id: Long) -> Unit,
    onCloseSearchBox: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(start = 20.dp),
            value = searchText,
            onValueChange = remoteNoteViewModel::onSearchTextChanged,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            trailingIcon = {
                IconButton(onClick = {
                    if (searchText.isNotBlank()) {
                        remoteNoteViewModel.onSearchTextChanged("")
                    } else {
                        onCloseSearchBox()
                    }
                }) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = stringResource(id = R.string.clear)
                    )
                }
            },
            label = { Text(text = stringResource(id = R.string.search_here)) }
        )
        Spacer(modifier = Modifier.height(16.dp))
        if (isSearching) {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        } else {
            NoteLazyColumn(noteItems) {
                onDetailsClick(it)
            }
        }
    }
}

@Composable
private fun DeleteDialog(remoteNoteViewModel: RemoteNoteViewModel, onDismissRequest: () -> Unit) {
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
                remoteNoteViewModel.deleteAllNotes()
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