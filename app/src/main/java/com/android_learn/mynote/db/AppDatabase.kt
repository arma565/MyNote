package com.android_learn.mynote.db

import android.content.Context
import androidx.fragment.app.FragmentActivity
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.android_learn.mynote.models.Note

@Database(entities = [Note::class], version = 3, exportSchema = false)
abstract class AppDatabase() : RoomDatabase() {
    abstract fun noteDao(): NoteDao
    companion object{
        private var instance : AppDatabase? = null

        @Synchronized
        fun getInstance(context: Context) : AppDatabase {
            if(instance == null)
            {
                instance = Room.databaseBuilder(context, AppDatabase::class.java, "note.db")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return instance!!
        }
    }
}