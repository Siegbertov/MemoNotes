package com.s1g1.memonotes

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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
    private var noteID: Int = -1

    private val noteViewModel: NoteViewModel by viewModels {
        NoteViewModelFactory((application as NoteApplication).repository)
    }
    private lateinit var binding: ActivityNoteDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        noteID = intent.getIntExtra("NOTE_ID", -1)

        binding = ActivityNoteDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarNoteDetails)
        configureNavigation()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupNoteDetails()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.note_details_menu, menu)
        if (noteID == -1) {
            menu?.findItem(R.id.action_delete)?.isVisible = false
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_save -> {
                val result = saveNote()
                closeEditor(result)
            }
            R.id.action_delete -> {
                val result = removeNote()
                closeEditor(result)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun configureNavigation() {
        binding.toolbarNoteDetails.setNavigationOnClickListener {
            closeEditor(true)
        }
    }

    private fun setupNoteDetails() {
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

    private fun saveNote() : Boolean {
        val noteTitle = binding.etNoteTitle.text.toString()
        val noteDescription = binding.etNoteDescription.text.toString()

        if (noteTitle.isNotBlank()){
            val newNote = NoteEntity(
                id = if (noteID==-1) 0 else noteID,
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

    private fun removeNote() : Boolean {
        if (noteID != -1){
            lifecycleScope.launch(Dispatchers.IO){
                noteViewModel.deleteNoteById(id=noteID)
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