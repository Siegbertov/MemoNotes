package com.s1g1.memonotes.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.s1g1.memonotes.database.NoteEntity
import com.s1g1.memonotes.databinding.ItemNoteBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NoteAdapter(
    private val onNoteClick: (NoteEntity) -> Unit,
    private val onSelectionChanged: (Int) -> Unit
) : ListAdapter<NoteEntity, NoteAdapter.NoteViewHolder>(NoteDiffCallback) {

    var isSelectionMode = false
    val selectedNotes = mutableSetOf<NoteEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = ItemNoteBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return NoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(getItem(position))

        val note = getItem(position)

        holder.itemView.isSelected = selectedNotes.contains(note)

        holder.itemView.setOnClickListener{
            if (isSelectionMode){
                toggleSelection(note)
            } else {
                onNoteClick(note)
            }
        }

        holder.itemView.setOnLongClickListener{
            if (!isSelectionMode){
                isSelectionMode = true
                toggleSelection(note)
            }
            true
        }

    }

    fun toggleSelection(note: NoteEntity){
        if (selectedNotes.contains(note)){
            selectedNotes.remove(note)
        } else {
            selectedNotes.add(note)
        }

        if (selectedNotes.isEmpty()) isSelectionMode = false
        val position = currentList.indexOf(note)
        if (position != -1){
            notifyItemChanged(position)
        }
        onSelectionChanged(selectedNotes.size)
    }

    fun clearSelection(){
        isSelectionMode = false
        selectedNotes.clear()
        notifyDataSetChanged()
        onSelectionChanged(0)
    }

    inner class NoteViewHolder(private val binding: ItemNoteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(note: NoteEntity) {
            binding.cardTitle.text = note.title
            binding.cardDescription.text = note.description

            val sdf = SimpleDateFormat("d MMM, HH:mm", Locale.getDefault())
            binding.cardTimestamp.text = sdf.format(Date(note.timestamp))

            binding.root.setOnClickListener {
                onNoteClick(note)
            }
        }
    }
}