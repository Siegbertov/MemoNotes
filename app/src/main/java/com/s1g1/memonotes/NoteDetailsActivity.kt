package com.s1g1.memonotes

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.s1g1.memonotes.databinding.ActivityNoteDetailsBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NoteDetailsActivity : AppCompatActivity() {

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

        val toolbar = binding.toolbarNoteDetails
        toolbar.setNavigationOnClickListener {
            closeEditor()
        }

        setCurrentDateTime()

        toolbar.inflateMenu(R.menu.note_details_menu)
        toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_save -> {
                    saveNote()
                    closeEditor()
                    true
                }
                else -> false
            }
        }
    }

    private fun saveNote() {
        val noteTitle = binding.etNoteTitle.text
        val noteDT = binding.tvNoteDateTime.text
        val noteDescription = binding.etNoteDescription.text

        println("TITLE: $noteTitle")
        println("DATETIME: $noteDT")
        println("DESCRIPTION: $noteDescription")
    }

    private fun setCurrentDateTime(){
        val sdf = SimpleDateFormat("d MMM, HH:mm:s", Locale.getDefault())
        val formattedDate = sdf.format(Date())

        binding.tvNoteDateTime.text = formattedDate
    }

    private fun closeEditor() {
        finish()
    }
}