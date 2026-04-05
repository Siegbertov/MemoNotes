package com.s1g1.memonotes.database

import androidx.room.TypeConverter

class NoteConverters {

    @TypeConverter
    fun fromNoteColor(color: NoteColor): String {
        return color.name
    }

    @TypeConverter
    fun toNoteColor(colorName: String): NoteColor {
        return NoteColor.fromName(colorName)
    }

}