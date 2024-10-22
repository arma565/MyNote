package com.note.mynote.view.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.note.mynote.R
import com.note.mynote.data.models.GlobalFunctions.checkNetwork
import com.note.mynote.data.models.Note
import com.note.mynote.view.activity.ui.theme.MyNoteTheme
import com.note.mynote.view.compose.AddFragmentComposeView
import com.note.mynote.view.compose.DetailsFragmentComposeView
import com.note.mynote.view.compose.EditFragmentComposeView
import com.note.mynote.view.compose.HomeFragmentComposeView
import com.note.mynote.viewmodel.RemoteNoteViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val remoteNoteViewModel: RemoteNoteViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val home = "home"
        val add = "add"
        val details = "details"
        val edit = "edit"
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            MyNoteTheme {
                NavHost(navController = navController, startDestination = home) {
                    composable(home) {
                        HomeFragmentComposeView(this@MainActivity,
                            remoteNoteViewModel = remoteNoteViewModel,
                            onAddClick = {
                                navController.navigate(add)
                            },
                            onDetailsClick = { id ->
                                navController.navigate("$details/$id")
                            })
                    }
                    composable(add) {
                        AddFragmentComposeView { note ->
                            remoteNoteViewModel.createNote(note)
                            startActivity(Intent(applicationContext, MainActivity::class.java))
                        }
                    }
                    composable(
                        route = "$details/{id}",
                        arguments = listOf(navArgument("id") {
                            type = NavType.IntType
                        })
                    ) { navBackStackEntry ->
                        val noteId: Int = navBackStackEntry.arguments?.getInt("id")!!
                        remoteNoteViewModel.getNote(noteId)
                        val note =
                            remoteNoteViewModel.getNoteStateFlow.collectAsState().value
                        if (note != Note()) {
                            DetailsFragmentComposeView(
                                remoteNoteViewModel = remoteNoteViewModel,
                                note = note,
                                onEditClick = { updatedNoteId ->
                                    navController.navigate("$edit/$updatedNoteId")
                                }, onShareClick = {
                                    val intentShare = Intent().apply {
                                        action = Intent.ACTION_SEND
                                        type = "text/plain"
                                        putExtra(
                                            Intent.EXTRA_SUBJECT,
                                            applicationContext.getString(R.string.myNote)
                                        )
                                        putExtra(
                                            Intent.EXTRA_TEXT, "Title: ${note.title} \n" +
                                                    "Description : ${note.description} \n " +
                                                    "Time: ${note.time} \n " +
                                                    "Date: ${note.date}"
                                        )
                                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                    }
                                    this@MainActivity.startActivity(
                                        Intent.createChooser(
                                            intentShare,
                                            applicationContext.getString(R.string.share)
                                        )
                                    )
                                }, onHomeClick = {
                                    startActivity(
                                        Intent(
                                            applicationContext,
                                            MainActivity::class.java
                                        )
                                    )
                                })
                        }
                    }
                    composable(
                        route = "$edit/{id}",
                        arguments = listOf(navArgument("id") {
                            type = NavType.IntType
                        })
                    ) { navBackStackEntry ->
                        val noteId = navBackStackEntry.arguments?.getInt("id")!!
                        remoteNoteViewModel.getNote(noteId)
                        val note =
                            remoteNoteViewModel.getNoteStateFlow.collectAsState().value
                        if (note != Note()) {
                            EditFragmentComposeView(
                                note = note,
                                onSave = { updatedNote ->
                                    remoteNoteViewModel.updateNote(
                                        updatedNote.id,
                                        updatedNote
                                    )
                                    startActivity(
                                        Intent(
                                            applicationContext,
                                            MainActivity::class.java
                                        )
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        checkNetwork(this@MainActivity) {}
    }
}