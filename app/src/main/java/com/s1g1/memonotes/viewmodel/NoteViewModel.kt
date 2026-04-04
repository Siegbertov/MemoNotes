package com.s1g1.memonotes.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.s1g1.memonotes.database.NoteEntity
import com.s1g1.memonotes.database.NoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoteViewModel(
    private val repository: NoteRepository
): ViewModel() {

    val allNotes = repository.allNotes.asLiveData()

    suspend fun getNoteById(id: Int) : NoteEntity{
        return repository.getNoteById(id=id)
    }

    fun upsert(noteEntity: NoteEntity){
        viewModelScope.launch(Dispatchers.IO) {
            repository.upsert(noteEntity=noteEntity)
        }
    }

    fun delete(noteEntity: NoteEntity){
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(noteEntity=noteEntity)
        }
    }

    fun deleteNoteById(id: Int){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteNoteById(id=id)
        }
    }

}


class NoteViewModelFactory(private val repository: NoteRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NoteViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NoteViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}