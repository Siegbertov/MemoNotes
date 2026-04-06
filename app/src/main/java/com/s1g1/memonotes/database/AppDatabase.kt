package com.s1g1.memonotes.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [NoteEntity::class],
    version = 3,
    exportSchema = false
)
@TypeConverters(NoteConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun noteEntityDao(): NoteEntityDao
}

val MIGRATION_1_2 = object : Migration(1, 2){
    override fun migrate(db: SupportSQLiteDatabase){
        db.execSQL("ALTER TABLE $NOTE_TABLE_NAME ADD COLUMN bgColor TEXT NOT NULL DEFAULT '${NoteColor.WHITE.name}'")
    }
}

val MIGRATION_2_3 = object : Migration(2, 3){
    override fun migrate(db: SupportSQLiteDatabase){
        db.execSQL("ALTER TABLE $NOTE_TABLE_NAME ADD COLUMN pinned INTEGER NOT NULL DEFAULT 0")
    }
}