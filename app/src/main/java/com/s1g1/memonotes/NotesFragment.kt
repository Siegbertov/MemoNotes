package com.s1g1.memonotes

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.s1g1.memonotes.adapters.NoteAdapter
import com.s1g1.memonotes.databinding.FragmentNotesBinding
import com.s1g1.memonotes.viewmodel.NoteViewModel
import com.s1g1.memonotes.viewmodel.NoteViewModelFactory

class NotesFragment : Fragment(R.layout.fragment_notes){

    private val noteViewModel: NoteViewModel by viewModels {
        NoteViewModelFactory((requireActivity().application as NoteApplication).repository)
    }

    private var _binding: FragmentNotesBinding? = null
    private val binding get() = _binding!!

    private lateinit var noteAdapter: NoteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentNotesBinding.bind(view)

        binding.fabAddNote.setOnClickListener {
            val intent = Intent(requireContext(), NoteDetailsActivity::class.java)
            intent.putExtra("NOTE_ID", -1)
            startActivity(intent)
        }

        setupRecyclerView()

        noteViewModel.allNotes.observe(viewLifecycleOwner) { notes ->
            if (notes.isEmpty()) {
                // future EMPTY IMPLEMENTATION
                println("THERE IS NO NOTE")
            } else {
                // future LIST IMPLEMENTATION
//                println("--- CURRENT NOTES ---")
//                notes.forEach { note ->
//                    println(note)
//                }
//                println("")

                noteAdapter.submitList(notes)
            }
        }

    }

    private fun setupRecyclerView() {
        noteAdapter = NoteAdapter { note ->
            val intent = Intent(requireContext(), NoteDetailsActivity::class.java)
            intent.putExtra("NOTE_ID", note.id)
            startActivity(intent)
        }

        binding.rvNotes.apply {
            adapter = noteAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}