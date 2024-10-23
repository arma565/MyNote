package com.note.mynote.data.models

import android.widget.Toast
import androidx.activity.ComponentActivity
import com.state.network.IResponseEvent
import com.state.network.NetworkStateManager

/**
 * Useful functions
 */
object GlobalFunctions {

    /**
     * Validate note
     * @param note instance of Note model
     */
    fun validateNote(note: Note, listener: IViewNoteResponse): Boolean {
        return when (true) {
            (note.title?.isEmpty() == true && note.description?.isEmpty() == true) -> {
                listener.onEmptyTitleAndDescription()
                false
            }

            note.title?.isEmpty() -> {
                listener.onEmptyTitle()
                false
            }

            note.description?.isEmpty() -> {
                listener.onEmptyDescription()
                false
            }

            else -> {
                true
            }
        }
    }

    fun checkNetwork(activity: ComponentActivity , isConnected : (state : Boolean) -> Unit) {
        NetworkStateManager(activity).start(object : IResponseEvent {
            override fun networkState(state: Boolean) {
                if (!state) {
                    Toast.makeText(
                        activity, "Network unavailable or server not respond. Please check your connection.",
                        Toast.LENGTH_SHORT
                    ).show()
                    isConnected(false)
                }else{
                    isConnected(true)
                }
            }
        })
    }
}