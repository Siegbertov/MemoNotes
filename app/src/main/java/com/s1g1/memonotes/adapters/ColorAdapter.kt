package com.s1g1.memonotes.adapters

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.s1g1.memonotes.R
import com.s1g1.memonotes.database.NoteColor

class ColorAdapter(
    private val colors: Array<NoteColor>,
    private val onColorSelected: (NoteColor) -> Unit,
) : RecyclerView.Adapter<ColorAdapter.ColorViewHolder>(){

    class ColorViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val colorView: View = view.findViewById(R.id.color_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_color_circle, parent, false)
        return ColorViewHolder(view)
    }

    override fun onBindViewHolder(holder: ColorViewHolder, position: Int) {
        val noteColor = colors[position]

        val colorInt = noteColor.colorRes

        holder.colorView.backgroundTintList = ColorStateList.valueOf(colorInt)

        holder.itemView.setOnClickListener {
            onColorSelected(noteColor)
        }
    }

    override fun getItemCount() = colors.size

}