package com.example.bscnotes.Fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.example.bscnotes.DataModel.Note
import com.example.bscnotes.R
import kotlinx.android.synthetic.main.fragment_edit_note.*

class NoteFragment : Fragment() {

    companion object{
        var note: Note? = null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_edit_note, container, false)
        setHasOptionsMenu(true)

        if (arguments?.isEmpty == false){
            note = arguments!!.get("note") as Note?
        }

        return v
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        activity!!.menuInflater.inflate(R.menu.note_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        et_note.setText(note?.noteText)
    }
}