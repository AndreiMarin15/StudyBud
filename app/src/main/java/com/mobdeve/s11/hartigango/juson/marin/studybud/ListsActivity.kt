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
        val docId = sp.getString("DOCID", null)

        setupRecyclerView(docId!!)

        binding.logoutBtn.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, MainActivity:: class.java))
            finish()
        }

        binding.calendarNav.setOnClickListener {
            val intent = Intent(this, CalendarActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            finish()
        }

        binding.dashboardNav.setOnClickListener {
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
            finish()
        }

        binding.addReminderbtn.setOnClickListener {
            val intent = Intent(this, ListsAddActivity:: class.java)
            startActivity(intent)

        }

    }

    /*
    This block handles the onClickListener for the "confirm" button in the user information form. It first checks if all the required fields (college, degree, and ID) are filled. If all fields are filled, it creates a new UserInfoModel object and calls Utility.setUserInfo() to add it to the Firestore database. It then queries the database to find the document with the user's email, retrieves its ID, and saves it to shared preferences along with the user's program. Finally, it starts the DashboardActivity.
    If any of the required fields are empty, it displays a toast message to remind the user to fill all fields.
    */
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