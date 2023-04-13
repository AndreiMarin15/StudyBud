package com.mobdeve.s11.hartigango.juson.marin.studybud

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query
import com.mobdeve.s11.hartigango.juson.marin.studybud.databinding.ActivityTasksBinding
import com.mobdeve.s11.hartigango.juson.marin.studybud.helpers.Utility
import com.mobdeve.s11.hartigango.juson.marin.studybud.models.ReminderModel
import com.mobdeve.s11.hartigango.juson.marin.studybud.models.TaskModel

class TasksActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTasksBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var sp: SharedPreferences
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        this.binding = ActivityTasksBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(this.binding.root)

        val category = intent.getStringExtra("category")!!
        auth = FirebaseAuth.getInstance()
        sp = applicationContext.getSharedPreferences("UserInfo", Context.MODE_PRIVATE)
        val docId = sp.getString("DOCID", null)!!
        recyclerView = binding.taskRecycler

        val mode = intent.getStringExtra("mode")

        when (mode) {
            "all" -> {
                setupRecyclerAllTodos(docId)

                binding.addTasksBtn.visibility = View.GONE
            }
            "inProgress" -> {
                setupRecyclerInProgress(docId)

                binding.addTasksBtn.visibility = View.GONE
            }
            "completed" -> {
                setupRecyclerCompleted(docId)

                binding.addTasksBtn.visibility = View.GONE
            }
            else -> {
                setupRecyclerView(docId, category)
            }
        }


        binding.titleTV.text = category

        binding.logoutBtn.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, MainActivity:: class.java))
            finish()
        }

        binding.calendarNav.setOnClickListener {
            val intent = Intent(this, CalendarActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.dashboardNav.setOnClickListener {
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.addTasksBtn.setOnClickListener {
            val intent = Intent(this, TaskAddActivity::class.java)
            intent.putExtra("category", category)
            startActivity(intent)
        }
    }

    private fun setupRecyclerView(docId: String, category: String) {
        val query = Utility.getCollectionReferenceForTasks(category, docId).orderBy("todoDate", Query.Direction.ASCENDING)
        val options: FirestoreRecyclerOptions<TaskModel> = FirestoreRecyclerOptions.Builder<TaskModel>()
            .setQuery(query, TaskModel::class.java).build()

        recyclerView.layoutManager = LinearLayoutManager(this)

        taskAdapter = TaskAdapter(options, this, docId, null)
        recyclerView.adapter = taskAdapter
    }

    private fun setupRecyclerAllTodos(docId: String){
        val query = Utility.getCollectionReferenceForAllTasks(docId).orderBy("todoDate", Query.Direction.ASCENDING)

        val options: FirestoreRecyclerOptions<TaskModel> = FirestoreRecyclerOptions.Builder<TaskModel>()
            .setQuery(query, TaskModel::class.java).build()

        recyclerView.layoutManager = LinearLayoutManager(this)

        taskAdapter = TaskAdapter(options, this, docId, null)
        recyclerView.adapter = taskAdapter
    }

    private fun setupRecyclerInProgress(docId: String){
        val query = Utility.getCollectionReferenceForAllTasks(docId).whereEqualTo("status", false).orderBy("todoDate", Query.Direction.ASCENDING)

        val options: FirestoreRecyclerOptions<TaskModel> = FirestoreRecyclerOptions.Builder<TaskModel>()
            .setQuery(query, TaskModel::class.java).build()

        recyclerView.layoutManager = LinearLayoutManager(this)

        taskAdapter = TaskAdapter(options, this, docId, null)
        recyclerView.adapter = taskAdapter
    }

    private fun setupRecyclerCompleted(docId: String){
        val query = Utility.getCollectionReferenceForAllTasks(docId).whereEqualTo("status", true).orderBy("todoDate", Query.Direction.ASCENDING)

        val options: FirestoreRecyclerOptions<TaskModel> = FirestoreRecyclerOptions.Builder<TaskModel>()
            .setQuery(query, TaskModel::class.java).build()

        recyclerView.layoutManager = LinearLayoutManager(this)

        taskAdapter = TaskAdapter(options, this, docId, null)
        recyclerView.adapter = taskAdapter
    }

    override fun onStart() {
        super.onStart()

        taskAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()

        taskAdapter.stopListening()
    }

    override fun onResume() {
        super.onResume()

        taskAdapter.notifyDataSetChanged()
    }
}