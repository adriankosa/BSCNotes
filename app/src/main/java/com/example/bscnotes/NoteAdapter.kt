package com.example.bscnotes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bscnotes.DataModel.Note
import kotlinx.android.synthetic.main.note_item.view.*

class NoteAdapter(private val list: List<Note>, val itemClickListener: OnItemClickListener)
    : RecyclerView.Adapter<NoteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note: Note = list[position]
        holder.bind(note, itemClickListener)
    }

    override fun getItemCount(): Int = list.size

}

class NoteViewHolder(itemView: View) :
    RecyclerView.ViewHolder(itemView) {

    val noteText = itemView.tv_note_text
    val noteDate = itemView.tv_note_date
    val noteDelete = itemView.ib_delete

    fun bind(note: Note, clickListener: OnItemClickListener) {
        noteText.text = note.noteText
        noteDate.text = note.getStringDate()

        noteText.setOnClickListener { clickListener.onNoteClicked(note) }
        noteDelete.setOnClickListener { clickListener.onDeleteClicked(note) }
    }

}

interface OnItemClickListener{
    fun onDeleteClicked(note: Note)
    fun onNoteClicked(note: Note)
}



