package com.note.mynote.data.models

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.NavHostFragment
import com.note.mynote.R
import com.note.mynote.view.activity.MainActivity

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

    fun getResult(activity: ComponentActivity, res: Long) {
        if (res > 0) {
            activity.finish()
            val intentMain = Intent(activity, MainActivity::class.java)
            intentMain.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            activity.startActivity(intentMain, activityFadeAnimation(activity as Context))
        } else {
            Toast.makeText(activity, activity.getString(R.string.unsuccessful), Toast.LENGTH_LONG)
                .show()
        }
    }

    private fun activityFadeAnimation(context: Context) = ActivityOptionsCompat.makeCustomAnimation(
        context,
        android.R.anim.fade_in,
        android.R.anim.fade_out
    ).toBundle()

/*    fun getNavControllerFragmentNote(activity: FragmentActivity) =
        (activity.supportFragmentManager.findFragmentById(R.id.fragmentContainerNote) as NavHostFragment).navController*/
}