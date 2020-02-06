package com.example.bscnotes.Fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bscnotes.DataModel.Note
import com.example.bscnotes.NoteAdapter
import com.example.bscnotes.OnItemClickListener
import com.example.bscnotes.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.fragment_list_of_notes.*

class ListFragment : Fragment(), OnItemClickListener {

    private val TAG = "ListFragment"
    val currentUser = FirebaseAuth.getInstance().currentUser
    val userID = currentUser!!.uid
    val usersCollection = FirebaseFirestore.getInstance().collection("users")
    val notesRef = usersCollection.document(userID).collection("notes")
    lateinit var addBtn: View

    private val notes: MutableList<Note> = mutableListOf()

    override fun onDeleteClicked(note: Note) {
        note.id?.let { notesRef.document(it).delete() }
    }

    override fun onNoteClicked(note: Note) {
        goToNoteFragment(note)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v =  inflater.inflate(R.layout.fragment_list_of_notes, container, false)
        setHasOptionsMenu(true)

        addBtn = v.findViewById<View>(R.id.floatingActionButton)

        addBtn.setOnClickListener {
            goToNoteFragment()
        }

        notesRef.orderBy("date", Query.Direction.DESCENDING).addSnapshotListener{data, error ->
            notes.clear()
            if (data != null) {
                data.forEach {
                    val note = Note(it.id, it.getString("noteText"), it.getDate("date"))
                    notes.add(note)
                }
            }
            recyclerView.apply {
                layoutManager = LinearLayoutManager(activity)
                adapter = NoteAdapter(notes, this@ListFragment)
            }

        }

        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fillRecyclerView()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        activity!!.menuInflater.inflate(R.menu.list_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_logout -> {
            FirebaseAuth.getInstance().signOut()
            fragmentManager!!.popBackStack()
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    fun fillRecyclerView(){

        notesRef.orderBy("date", Query.Direction.DESCENDING).get().addOnSuccessListener {

            if (!it.isEmpty){
                        notes.clear()
                        it.forEach {
                            val note = Note(it.id, it.getString("noteText"), it.getDate("date"))
                            notes.add(note)
                        }
                        recyclerView.apply {
                            layoutManager = LinearLayoutManager(activity)
                            adapter = NoteAdapter(notes, this@ListFragment)
                        }
                }
            }
            .addOnFailureListener {
            Log.e(TAG, "Error getting notes: ", it)
        }
    }

    private fun goToNoteFragment(note: Note? = null){

        val noteFrament = NoteFragment()

        if(note != null){
            val bundle = Bundle()
            bundle.putParcelable("note", note)
            noteFrament.arguments = bundle
        }

        val tx = fragmentManager!!.beginTransaction()
        tx.add(R.id.containerFragment, noteFrament).addToBackStack(null).commit()
    }

}

