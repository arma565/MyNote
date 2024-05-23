package com.note.mynote.di

import android.content.Context
import androidx.room.Room
import com.note.mynote.data.local.AppDatabase
import com.note.mynote.data.local.NoteDao
import com.note.mynote.data.repository.IRepository
import com.note.mynote.data.repository.NoteRepositoryImp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt Module
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private const val DB_NAME = "note.db"

    @Singleton
    @Provides
    fun provideDataBase(@ApplicationContext context: Context) =
        synchronized(AppDatabase::class) {
            Room.databaseBuilder(context, AppDatabase::class.java, DB_NAME).build()
        }

    @Singleton
    @Provides
    fun provideDao(database: AppDatabase) = database.noteDao()

    @Singleton
    @Provides
    fun provideRepositoryImp(noteDao: NoteDao): IRepository = NoteRepositoryImp(noteDao)
}