package com.s1g1.memonotes

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.s1g1.memonotes.databinding.FragmentNotesBinding
import com.s1g1.memonotes.viewmodel.NoteViewModel
import com.s1g1.memonotes.viewmodel.NoteViewModelFactory

class NotesFragment : Fragment(R.layout.fragment_notes){

    private val noteViewModel: NoteViewModel by viewModels {
        NoteViewModelFactory((requireActivity().application as NoteApplication).repository)
    }

    private var _binding: FragmentNotesBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentNotesBinding.bind(view)

        binding.fabAddNote.setOnClickListener {
            val intent = Intent(requireContext(), NoteDetailsActivity::class.java)
            intent.putExtra("NOTE_ID", -1)
            startActivity(intent)
        }

        noteViewModel.allNotes.observe(viewLifecycleOwner) { notes ->
            if (notes.isEmpty()) {
                // future EMPTY IMPLEMENTATION
                println("THERE IS NO NOTE")
            } else {
                // future LIST IMPLEMENTATION
                println("--- CURRENT NOTES ---")
                notes.forEach { note ->
                    println(note)
                }
                println("")
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}