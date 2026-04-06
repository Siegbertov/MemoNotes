package com.s1g1.memonotes.database

import kotlinx.coroutines.flow.Flow

class NoteRepository(private val noteDao: NoteEntityDao) {

    val allNotes: Flow<List<NoteEntity>> = noteDao.getAllNotes()

    suspend fun getNoteById(id: Int) : NoteEntity{
        return noteDao.getNoteByID(id=id)
    }

    suspend fun upsert(noteEntity: NoteEntity){
        noteDao.upsertNote(noteEntity=noteEntity)
    }

    suspend fun delete(noteEntity: NoteEntity){
        noteDao.deleteNote(noteEntity=noteEntity)
    }

    suspend fun deleteNotes(notes: List<NoteEntity>){
        noteDao.deleteNotes(notes=notes)
    }

    suspend fun deleteNoteById(id: Int){
        return noteDao.deleteNoteById(id=id)
    }

    fun updateNotesColor(selectedIds: List<Int>, selectedColor: NoteColor) {
        return noteDao.updateNotesColor(selectedIds=selectedIds, selectedColor=selectedColor)
    }

    fun changePinState(selectedIds: List<Int>) {
        return noteDao.changePinState(selectedIds=selectedIds)
    }
}