package com.mobdeve.s11.hartigango.juson.marin.studybud

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query
import com.mobdeve.s11.hartigango.juson.marin.studybud.databinding.ActivityRemindersBinding
import com.mobdeve.s11.hartigango.juson.marin.studybud.helpers.Utility
import com.mobdeve.s11.hartigango.juson.marin.studybud.models.ReminderModel

class RemindersActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRemindersBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var sp: SharedPreferences
    private lateinit var reminderAdapter: ReminderAdapter



    override fun onCreate(savedInstanceState: Bundle?) {
        this.binding = ActivityRemindersBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(this.binding.root)
        sp = applicationContext.getSharedPreferences("UserInfo", Context.MODE_PRIVATE)
        val docId = sp.getString("DOCID", null)
        recyclerView = binding.recyclerView

        setupRecyclerView(docId!!)

        binding.addReminderbtn.setOnClickListener{
            val intent = Intent(this, ReminderAddActivity::class.java)
            startActivity(intent)
        }
    }

    /*
    This code sets up a RecyclerView to display reminders for a document in Firestore. It creates a Firestore query that orders the reminders by date and time and builds a FirestoreRecyclerOptions object.

    It then sets the layout manager and adapter for the RecyclerView using a ReminderAdapter.
    */
    private fun setupRecyclerView(docId: String) {
        val query = Utility.getCollectionReferenceForReminders(docId).orderBy("dateTime", Query.Direction.ASCENDING)
        val options: FirestoreRecyclerOptions<ReminderModel> = FirestoreRecyclerOptions.Builder<ReminderModel>()
            .setQuery(query, ReminderModel::class.java).build()

        recyclerView.layoutManager = LinearLayoutManager(this)
        reminderAdapter = ReminderAdapter(options, this, docId)
        recyclerView.adapter = reminderAdapter

    }

    override fun onStart() {
        super.onStart()

        reminderAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()

        reminderAdapter.stopListening()
    }

    override fun onResume() {
        super.onResume()

        reminderAdapter.notifyDataSetChanged()
    }
}