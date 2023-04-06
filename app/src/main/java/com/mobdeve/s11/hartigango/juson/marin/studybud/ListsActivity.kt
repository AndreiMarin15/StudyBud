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
import com.mobdeve.s11.hartigango.juson.marin.studybud.*
import com.mobdeve.s11.hartigango.juson.marin.studybud.databinding.ActivityListsBinding
import com.mobdeve.s11.hartigango.juson.marin.studybud.helpers.Utility
import com.mobdeve.s11.hartigango.juson.marin.studybud.models.ListModel

class ListsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityListsBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var recyclerView: RecyclerView
    private lateinit var listsAdapter: ListsAdapter
    private lateinit var sp: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        this.binding = ActivityListsBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(this.binding.root)
        recyclerView = binding.recyclerView

        auth = FirebaseAuth.getInstance()
        sp = applicationContext.getSharedPreferences("UserInfo", Context.MODE_PRIVATE)
        val docId = sp.getString("DOCID", "N/A")

        setupRecyclerView(docId!!)

        binding.logoutBtn.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, MainActivity:: class.java))
            finish()
        }

        binding.calendarNav.setOnClickListener {
            val intent = Intent(this, CalendarActivity::class.java)
            startActivity(intent)

        }

        binding.dashboardNav.setOnClickListener {
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
        }

        binding.addReminderbtn.setOnClickListener {
            val intent = Intent(this, AddTaskActivity:: class.java)
            startActivity(intent)
        }

    }

    private fun setupRecyclerView(docID: String) {
        val query = Utility.getCollectionReferenceForLists(docID)
            .orderBy("name", Query.Direction.ASCENDING)

        val options: FirestoreRecyclerOptions<ListModel> = FirestoreRecyclerOptions.Builder<ListModel>()
            .setQuery(query, ListModel::class.java)
            .build()

        recyclerView.layoutManager = LinearLayoutManager(this)
        listsAdapter = ListsAdapter(options, this, docID)
        recyclerView.adapter = listsAdapter
    }


    override fun onStart() {
        super.onStart()

        listsAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()

        listsAdapter.stopListening()
    }

    override fun onResume() {
        super.onResume()

        listsAdapter.notifyDataSetChanged()
    }

}