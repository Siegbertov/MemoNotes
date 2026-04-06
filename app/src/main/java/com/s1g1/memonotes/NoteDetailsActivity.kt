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
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.getValue

class NoteDetailsActivity : AppCompatActivity() {

    private var noteID: Int = -1

    private var noteTimestamp: Long = System.currentTimeMillis()

    private var currentNote: NoteEntity = NoteEntity(
        timestamp = noteTimestamp
    )


    private val noteViewModel: NoteViewModel by viewModels {
        NoteViewModelFactory((application as NoteApplication).repository)
    }
    private lateinit var binding: ActivityNoteDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNoteDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarNoteDetails)
        configureNavigation()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        updateNoteID()
        updateNoteAndShow()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.note_details_menu, menu)
        if (noteID == -1) {
            menu.findItem(R.id.action_delete)?.isVisible = false
        }
        val brushItem = menu.findItem(R.id.action_color_picker)
        brushItem?.icon?.let{ icon ->
            val drawable = icon.mutate()
            drawable.setTint(currentNote.bgColor.colorRes)
            brushItem.icon = drawable
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_save -> {
                saveNote()
            }
            R.id.action_delete -> {
                removeNote()
            }
            R.id.action_color_picker -> {
                changeNoteColor()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun changeNoteColor() {
        val nextColor = currentNote.bgColor.getNext()
        currentNote = currentNote.copy(bgColor = nextColor)
        binding.root.setBackgroundColor(currentNote.bgColor.colorRes)
        invalidateOptionsMenu()
    }

    private fun configureNavigation() {
        binding.toolbarNoteDetails.setNavigationOnClickListener {
            closeEditor()
        }
    }

    private fun updateNoteID(){
        noteID = intent.getIntExtra("NOTE_ID", -1)
    }

    private fun updateNoteAndShow(){
        if(noteID != -1){
            lifecycleScope.launch(Dispatchers.IO){
                val noteFromDb = noteViewModel.getNoteById(noteID)

                withContext(Dispatchers.Main) {
                    currentNote = noteFromDb
                    updateNoteDetails()
                }

            }
        }
        binding.root.setBackgroundColor(currentNote.bgColor.colorRes)
    }

    private fun updateNoteDetails() {
        binding.etNoteTitle.setText(currentNote.title)
        setCurrentDateTime()
        binding.etNoteDescription.setText(currentNote.description)
        binding.toolbarNoteDetails.title = if(currentNote.id!=-1) getString(R.string.toolbar_title_edit) else getString(R.string.toolbar_title_new)

    }

    private fun setCurrentDateTime(){
        val sdf = SimpleDateFormat("d MMM, HH:mm:ss", Locale.getDefault())
        val formattedDate = sdf.format(Date(currentNote.timestamp))
        binding.tvNoteDateTime.text = formattedDate
    }

    private fun saveNote() {
        val noteTitle = binding.etNoteTitle.text.toString()
        val noteDescription = binding.etNoteDescription.text.toString()

        if (noteTitle.isNotBlank()){
            lifecycleScope.launch(Dispatchers.IO){
                noteViewModel.upsert(
                    noteEntity=currentNote.copy(
                        title = noteTitle,
                        description = noteDescription
                    )
                )
                withContext(Dispatchers.Main){
                    closeEditor()
                }
            }
        }
    }

    private fun removeNote() {
        if (noteID != -1){
            lifecycleScope.launch(Dispatchers.IO){
                noteViewModel.delete(currentNote)
                withContext(Dispatchers.Main){
                    closeEditor()
                }
            }
        }
    }

    private fun closeEditor() {
        finish()
    }
}