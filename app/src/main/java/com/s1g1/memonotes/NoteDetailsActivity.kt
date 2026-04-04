package com.s1g1.memonotes

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.s1g1.memonotes.database.NoteEntity
import com.s1g1.memonotes.databinding.ActivityNoteDetailsBinding
import com.s1g1.memonotes.viewmodel.NoteViewModel
import com.s1g1.memonotes.viewmodel.NoteViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.getValue

class NoteDetailsActivity : AppCompatActivity() {

    private var noteTimestamp: Long = System.currentTimeMillis()

    private val noteViewModel: NoteViewModel by viewModels {
        NoteViewModelFactory((application as NoteApplication).repository)
    }
    private lateinit var binding: ActivityNoteDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNoteDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val noteId = intent.getIntExtra("NOTE_ID", -1)
        setupNoteDetails(noteID = noteId)
        setupToolbar(noteId=noteId)

    }

    private fun setupToolbar(noteId: Int) {
        val toolbar = binding.toolbarNoteDetails
        toolbar.setNavigationOnClickListener {
            closeEditor(true)
        }

        toolbar.inflateMenu(R.menu.note_details_menu)
        if (noteId == -1) {
            toolbar.menu.findItem(R.id.action_delete)?.isVisible = false
        }
        toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_save -> {
                    val result = saveNote(noteId=noteId)
                    closeEditor(result)
                    true
                }
                R.id.action_delete -> {
                    val result = removeNote(noteId=noteId)
                    closeEditor(result)
                    true
                }
                else -> false
            }
        }
    }

    private fun setupNoteDetails(noteID: Int) {
        setCurrentDateTime()
        if (noteID != -1){
            lifecycleScope.launch(Dispatchers.IO) {
                val currentNote = noteViewModel.getNoteById(id = noteID)
                binding.etNoteTitle.setText(currentNote.title)
                binding.etNoteDescription.setText(currentNote.description)
                binding.toolbarNoteDetails.title = getString(R.string.toolbar_title_edit)
            }
        } else {
            binding.toolbarNoteDetails.title = getString(R.string.toolbar_title_new)
        }
    }

    private fun setCurrentDateTime(){
        val sdf = SimpleDateFormat("d MMM, HH:mm:ss", Locale.getDefault())
        val formattedDate = sdf.format(Date(noteTimestamp))
        binding.tvNoteDateTime.text = formattedDate
    }

    private fun saveNote(noteId: Int) : Boolean {
        val noteTitle = binding.etNoteTitle.text.toString()
        val noteDescription = binding.etNoteDescription.text.toString()

        if (noteTitle.isNotBlank()){
            val newNote = NoteEntity(
                id = if (noteId==-1) 0 else noteId,
                title = noteTitle,
                description = noteDescription,
                timestamp = noteTimestamp
            )
            lifecycleScope.launch(Dispatchers.IO){
                noteViewModel.upsert(noteEntity=newNote)
            }
            return true
        }
        return false
    }

    private fun removeNote(noteId: Int) : Boolean {
        if (noteId != -1){
            lifecycleScope.launch(Dispatchers.IO){
                noteViewModel.deleteNoteById(id=noteId)
            }
            return true
        }
        return false
    }

    private fun closeEditor(shouldClose: Boolean) {
        if (shouldClose){
            finish()
        }
    }
}