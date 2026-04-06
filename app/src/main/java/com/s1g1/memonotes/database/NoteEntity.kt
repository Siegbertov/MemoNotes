package com.s1g1.memonotes.database

import androidx.room.Entity
import androidx.room.PrimaryKey

const val NOTE_TABLE_NAME = "notes_table"

@Entity(tableName = NOTE_TABLE_NAME)
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String = "",
    val description: String = "",
    val timestamp: Long,
    val bgColor: NoteColor = NoteColor.WHITE,
    val pinned: Boolean = false,
)

enum class NoteColor(val colorRes: Int){
    WHITE(0xFFFFFFFF.toInt()),
    RED(0xFFF28B82.toInt()),
    ORANGE(0xFFFBBC04.toInt()),
    YELLOW(0xFFFFF475.toInt()),
    GREEN(0xFFCCFF90.toInt()),
    BLUE(0xFFAECBFA.toInt()),
    PURPLE(0xFFD7AEFB.toInt());


    fun getNext(): NoteColor{
        val nextIndex = (ordinal + 1) % entries.size
        return entries[nextIndex]
    }
    companion object {
        fun fromName(name: String?): NoteColor =
            entries.find { it.name == name } ?: WHITE
    }
}


