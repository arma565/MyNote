package com.note.mynote.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.note.mynote.data.models.Note

/**
 * App Database
 */
@Database(entities = [Note::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
}