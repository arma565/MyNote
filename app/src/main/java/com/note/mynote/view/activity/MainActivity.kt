package com.note.mynote.view.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.note.mynote.R
import com.note.mynote.data.models.Note
import com.note.mynote.view.fragments.add.AddFragmentComposeView
import com.note.mynote.view.fragments.detail.DetailsFragmentComposeView
import com.note.mynote.view.fragments.detail.edit.EditFragmentComposeView
import com.note.mynote.view.fragments.home.HomeFragmentComposeView
import com.note.mynote.viewmodel.NoteViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val noteViewModel: NoteViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "homeFragment") {
                composable("homeFragment") {
                    HomeFragmentComposeView(noteViewModel = noteViewModel,
                        onAddClick = {
                            navController.navigate("addFragment")
                        }, onDetailsClick = {
                            navController.navigate("detailsFragment/$it")
                        })
                }
                composable("addFragment") {
                    AddFragmentComposeView {
                        noteViewModel.upsertNote(it)
                        navController.navigate("homeFragment")
                    }
                }
                composable(
                    route = "detailsFragment/{id}",
                    arguments = listOf(navArgument("id") {
                        type = NavType.IntType
                    })
                ) { navBackStackEntry ->
                    val noteId = navBackStackEntry.arguments?.getInt("id")
                    noteViewModel.getSpecificNote(noteId!!)
                    val note: Note = noteViewModel.getNote
                    if (note != Note()) {
                        DetailsFragmentComposeView(
                            noteViewModel = noteViewModel,
                            note = note,
                            onEditClick = {
                                navController.navigate("editFragment/$it")
                            }, onShareClick = {
                                val intentShare = Intent()
                                intentShare.action = Intent.ACTION_SEND
                                intentShare.type = "text/plain"
                                val shareBody = "Title: ${note.title} \n" +
                                        "Description : ${note.description} \n " +
                                        "Time: ${note.time} \n " +
                                        "Date: ${note.date}"
                                intentShare.putExtra(
                                    Intent.EXTRA_SUBJECT,
                                    applicationContext.getString(R.string.myNote)
                                )
                                intentShare.putExtra(Intent.EXTRA_TEXT, shareBody)
                                intentShare.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                this@MainActivity.startActivity(
                                    Intent.createChooser(
                                        intentShare,
                                        "Share via"
                                    )
                                )
                            }, onHomeClick = {
                                navController.navigate("homeFragment")
                            })
                    }
                }
                composable(
                    route = "editFragment/{id}",
                    arguments = listOf(navArgument("id") {
                        type = NavType.IntType
                    })
                ) { navBackStackEntry ->
                    val noteId = navBackStackEntry.arguments?.getInt("id")
                    noteViewModel.getSpecificNote(noteId!!)
                    val note: Note = noteViewModel.getNote
                    if (note != Note()) {
                        EditFragmentComposeView(
                            context = applicationContext,
                            parentFragmentManager = supportFragmentManager,
                            note = note,
                            onSave = { updatedNote ->
                                noteViewModel.upsertNote(updatedNote)
                                navController.navigate("homeFragment")
                            }
                        )
                    }
                }
            }
        }
    }
}