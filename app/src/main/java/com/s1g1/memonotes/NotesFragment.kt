package com.s1g1.memonotes

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.s1g1.memonotes.adapters.ColorAdapter
import com.s1g1.memonotes.adapters.NoteAdapter
import com.s1g1.memonotes.database.NoteColor
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
        val toolbar = requireActivity().findViewById<MaterialToolbar>(R.id.toolbar_main)

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
                    val selectedNotes = noteAdapter.selectedNotes.toList()
                    if (selectedNotes.isNotEmpty()){
                        val dialog = BottomSheetDialog(requireContext())
                        val view = layoutInflater.inflate(R.layout.dialog_color_picker, null)
                        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_colors)
                        recyclerView.layoutManager = GridLayoutManager(requireContext(), 5)
                        recyclerView.adapter = ColorAdapter(
                            colors = NoteColor.entries.toTypedArray(),
                            onColorSelected = { selectedColor ->
                                val selectedIds = selectedNotes.map{it.id}
                                noteViewModel.updateNotesColor(selectedIds, selectedColor)
                                noteAdapter.clearSelection()
                                dialog.dismiss()
                            }
                        )
                        dialog.setContentView(view)
                        dialog.show()
                    }
                    true
                }
                else -> false
            }
        }

    }

    private fun setupRecyclerView() {
        val toolbar = requireActivity().findViewById<MaterialToolbar>(R.id.toolbar_main)
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

                toolbar.menu.findItem(R.id.action_delete_selected).isVisible = visibilityMode

                toolbar.menu.findItem(R.id.action_paint_selected).isVisible = visibilityMode

                binding.fabAddNote.isVisible = !visibilityMode
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