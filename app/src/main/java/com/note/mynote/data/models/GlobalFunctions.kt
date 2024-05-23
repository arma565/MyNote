package com.note.mynote.data.models

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
}