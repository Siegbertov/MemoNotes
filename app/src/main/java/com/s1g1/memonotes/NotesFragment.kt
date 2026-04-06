package com.s1g1.memonotes

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
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
        val toolbar = requireActivity().findViewById<com.google.android.material.appbar.MaterialToolbar>(R.id.toolbar_main)

        binding.fabAddNote.setOnClickListener {
            val intent = Intent(requireContext(), NoteDetailsActivity::class.java)
            intent.putExtra("NOTE_ID", -1)
            startActivity(intent)
        }

        setupRecyclerView()

        noteViewModel.allNotes.observe(viewLifecycleOwner) { notes ->
            noteAdapter.submitList(notes)

            if (notes.isEmpty()) {
                binding.rvNotes.visibility = View.GONE
                binding.tvEmptyView.visibility = View.VISIBLE
            } else {
                binding.rvNotes.visibility = View.VISIBLE
                binding.tvEmptyView.visibility = View.GONE
            }
        }

        toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_delete_selected -> {
                    val selectedNotes = noteAdapter.selectedNotes.toList()
                    if (selectedNotes.isNotEmpty()) {
                        noteViewModel.deleteNotes(selectedNotes)
                        noteAdapter.clearSelection() // Сбрасываем выбор в UI
                    }
                    true
                }
                R.id.action_paint_selected -> {
                    Toast.makeText(requireContext(), "PRESSED PAINT", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }

    }

    private fun setupRecyclerView() {
        val toolbar = requireActivity().findViewById<com.google.android.material.appbar.MaterialToolbar>(R.id.toolbar_main)
        noteAdapter = NoteAdapter(
            onNoteClick = { note ->
                val intent = Intent(requireContext(), NoteDetailsActivity::class.java)
                intent.putExtra("NOTE_ID", note.id)
                startActivity(intent)
            },
            onSelectionChanged = {count ->

                val visibilityMode = count > 0

                if (visibilityMode) {
                    toolbar.navigationIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_clear)

                    toolbar.setNavigationOnClickListener {
                        noteAdapter.clearSelection()
                    }
                    toolbar.title = "Chosen: $count"
                } else {
                    toolbar.navigationIcon = null
                    toolbar.title = getString(R.string.app_name)
                }

                val deleteMenuItem = toolbar.menu.findItem(R.id.action_delete_selected)
                deleteMenuItem.isVisible = visibilityMode

                val paintMenuItem = toolbar.menu.findItem(R.id.action_paint_selected)
                paintMenuItem.isVisible = visibilityMode
            }
        )

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