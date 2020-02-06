package com.example.bscnotes.Activities

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.bscnotes.DataModel.Note
import com.example.bscnotes.Fragments.ListFragment
import com.example.bscnotes.Fragments.LoginFragment
import com.example.bscnotes.Fragments.NoteFragment
import com.example.bscnotes.R
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"

    val usersCollection = FirebaseFirestore.getInstance().collection("users")
    val currentUser = FirebaseAuth.getInstance().currentUser
    var userID: String = ""

    private val fragmentManager = supportFragmentManager
    private val loginFragment = LoginFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.containerFragment, loginFragment)
        fragmentTransaction.commit()

    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBackPressed() {

        val activeFragment = supportFragmentManager.findFragmentById(R.id.containerFragment)
        when (activeFragment) {

            is ListFragment -> {
                this.finish()
                System.exit(0)
            }

            is NoteFragment -> {

                val editField = activeFragment.view?.findViewById<TextInputEditText>(R.id.et_note)

                if (userID.isEmpty()) {
                    userID = currentUser!!.uid
                }

                if (NoteFragment.note == null){
                    if (editField?.text?.isBlank() == false) {
                        editField.text?.toString()?.let {
                            usersCollection.document(userID).collection("notes").add(
                                Note(null ,it, Date())
                            ).addOnSuccessListener {
                                super.onBackPressed()
                            }.addOnFailureListener {
                                Log.e(TAG, "Error writting new document: ", it)
                            }
                        }
                    } else super.onBackPressed()
                }else{
                    val noteId = NoteFragment.note!!.id

                    if (editField?.text?.isBlank() == false) {
                        editField.text?.toString()?.let {

                            if (noteId != null) {
                                usersCollection.document(userID).collection("notes").document(noteId).update("noteText", it)
                                    .addOnSuccessListener {
                                        NoteFragment.note = null
                                        super.onBackPressed()
                                    }.addOnFailureListener {
                                        Log.e(TAG, "Error writting new document: ", it)
                                    }
                            }

                        }
                    }else{

                        if (noteId != null) {
                            usersCollection.document(userID).collection("notes").document(
                                noteId
                            ).delete().addOnSuccessListener { super.onBackPressed() }
                        }

                    }
                }

            }

            else -> super.onBackPressed()
        }
    }
}
