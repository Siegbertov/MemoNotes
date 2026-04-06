package com.s1g1.memonotes

import android.app.Application
import androidx.room.Room
import com.s1g1.memonotes.database.AppDatabase
import com.s1g1.memonotes.database.MIGRATION_1_2
import com.s1g1.memonotes.database.MIGRATION_2_3
import com.s1g1.memonotes.database.NoteRepository

class NoteApplication : Application() {

    val db: AppDatabase by lazy{
        Room.databaseBuilder(
            context = this,
            klass = AppDatabase::class.java,
            name = "notes.db"
        )
            .addMigrations(
                MIGRATION_1_2,
                MIGRATION_2_3
            )
            .build()
    }

    val repository by lazy {
        NoteRepository(noteDao = db.noteEntityDao())
    }

}