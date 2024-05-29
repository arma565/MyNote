package com.note.mynote.view.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.note.mynote.R
import com.note.mynote.data.models.Note
import com.note.mynote.view.activity.ui.theme.MyNoteTheme
import com.note.mynote.view.compose.AddFragmentComposeView
import com.note.mynote.view.compose.DetailsFragmentComposeView
import com.note.mynote.view.compose.EditFragmentComposeView
import com.note.mynote.view.compose.HomeFragmentComposeView
import com.note.mynote.viewmodel.NoteViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val noteViewModel: NoteViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val home = "home"
        val add = "add"
        val details = "details"
        val edit = "edit"
        enableEdgeToEdge()
        setContent {
            MyNoteTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = home) {
                    composable(home) {
                        HomeFragmentComposeView(noteViewModel = noteViewModel,
                            onAddClick = {
                                navController.navigate(add)
                            }, onDetailsClick = {
                                navController.navigate("$details/$it")
                            })
                    }
                    composable(add) {
                        AddFragmentComposeView {
                            noteViewModel.upsertNote(it)
                            navController.navigate(home)
                        }
                    }
                    composable(
                        route = "$details/{id}",
                        arguments = listOf(navArgument("id") {
                            type = NavType.IntType
                        })
                    ) { navBackStackEntry ->
                        val noteId = navBackStackEntry.arguments?.getInt("id")
                        val note: Note = noteViewModel.getSpecificNote(noteId!!)
                        if (note != Note()) {
                            DetailsFragmentComposeView(
                                noteViewModel = noteViewModel,
                                note = note,
                                onEditClick = {
                                    navController.navigate("$edit/$it")
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
                                    navController.navigate(home)
                                })
                        }
                    }
                    composable(
                        route = "$edit/{id}",
                        arguments = listOf(navArgument("id") {
                            type = NavType.IntType
                        })
                    ) { navBackStackEntry ->
                        val noteId = navBackStackEntry.arguments?.getInt("id")
                        val note: Note = noteViewModel.getSpecificNote(noteId!!)
                        if (note != Note()) {
                            EditFragmentComposeView(
                                note = note,
                                onSave = { updatedNote ->
                                    noteViewModel.upsertNote(updatedNote)
                                    navController.navigate(home)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}