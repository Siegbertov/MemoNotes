package com.s1g1.memonotes

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.s1g1.memonotes.databinding.FragmentNotesBinding

class NotesFragment : Fragment(R.layout.fragment_notes){
    private var _binding: FragmentNotesBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentNotesBinding.bind(view)

        binding.fabAddNote.setOnClickListener {
            val intent = Intent(requireContext(), NoteDetailsActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}