package com.note.mynote.view.fragments.detail

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import com.note.mynote.R
import com.note.mynote.databinding.FragmentDetailsBinding
import com.note.mynote.viewmodel.NoteViewModel
import com.note.mynote.data.models.GlobalFunctions
import com.note.mynote.data.models.Note
import dagger.hilt.android.AndroidEntryPoint

/**
 * Details
 */
@Suppress("DEPRECATION")
@AndroidEntryPoint
class DetailsFragment : Fragment() {
    private lateinit var binding: FragmentDetailsBinding
    private lateinit var note: Note
    private lateinit var navController: NavController
    private lateinit var owner: LifecycleOwner
    private val noteVieModel: NoteViewModel by viewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        owner = this
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_details, container, false)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            note = arguments?.getParcelable("note", Note::class.java)!!
        }
        note = arguments?.getParcelable("note")!!
        binding.note = note
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = GlobalFunctions.getNavControllerFragmentNote(requireActivity())

        val mainActivity: AppCompatActivity = requireActivity() as AppCompatActivity

        mainActivity.setSupportActionBar(binding.detailsToolbar)
        binding.detailsToolbar.title = ""

        (mainActivity as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menu.clear()
                menuInflater.inflate(R.menu.detail_menu, menu)

                val deleteItem: MenuItem = menu.findItem(R.id.item_deleteDetails)
                val editItem: MenuItem = menu.findItem(R.id.item_editDetails)
                val shareItem: MenuItem = menu.findItem(R.id.item_shareDetails)

                deleteItem.setOnMenuItemClickListener {
                    val alertDialog = AlertDialog.Builder(requireActivity())
                    alertDialog.setTitle(getString(R.string.delete))
                    alertDialog.setMessage(R.string.are_you_sure)
                    alertDialog.setPositiveButton(getString(R.string.yes)) { _, _ ->
                        noteVieModel.deleteNote(note)
                        GlobalFunctions.getResult(mainActivity,1)
                    }
                    alertDialog.setNegativeButton(getString(R.string.no)) { _, _ -> }
                    alertDialog.show()
                    true
                }

                editItem.setOnMenuItemClickListener {
                    navController.navigate(R.id.action_detailsFragment_to_editFragment, bundleOf("note" to note))
                    true
                }

                shareItem.setOnMenuItemClickListener {
                    val intentShare = Intent()
                    intentShare.action = Intent.ACTION_SEND
                    intentShare.type = "text/plain"
                    val shareBody = "Title: ${note.title} \n" +
                            "Description : ${note.description} \n " +
                            "Time: ${note.time} \n " +
                            "Date: ${note.date}"
                    intentShare.putExtra(Intent.EXTRA_SUBJECT, requireActivity().getString(R.string.myNote))
                    intentShare.putExtra(Intent.EXTRA_TEXT, shareBody)
                    intentShare.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    requireActivity().startActivity(Intent.createChooser(intentShare, "Share via"))
                    true
                }
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return false
            }

        })

        binding.imgClose.setOnClickListener {
           GlobalFunctions.getResult(requireActivity() as AppCompatActivity,1)
        }
    }
}