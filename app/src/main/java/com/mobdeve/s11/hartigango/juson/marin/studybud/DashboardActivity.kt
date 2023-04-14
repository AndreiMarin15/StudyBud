package com.mobdeve.s11.hartigango.juson.marin.studybud

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.CheckBox
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import com.mobdeve.s11.hartigango.juson.marin.studybud.databinding.ActivityDashboardBinding
import com.mobdeve.s11.hartigango.juson.marin.studybud.helpers.CircleTransform
import com.mobdeve.s11.hartigango.juson.marin.studybud.helpers.Utility
import com.mobdeve.s11.hartigango.juson.marin.studybud.models.ListModel
import com.mobdeve.s11.hartigango.juson.marin.studybud.models.ReminderModel
import com.mobdeve.s11.hartigango.juson.marin.studybud.models.TaskModel
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class DashboardActivity: AppCompatActivity() {
    private lateinit var reminderAdapter: ReminderAdapter
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var reminderRecycler: RecyclerView
    private lateinit var recyclerView2: RecyclerView
    private lateinit var binding: ActivityDashboardBinding
    private lateinit var docId: String
    private lateinit var auth: FirebaseAuth
    private lateinit var sp: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(this.binding.root)

        auth = FirebaseAuth.getInstance()
        sp = applicationContext.getSharedPreferences("UserInfo", Context.MODE_PRIVATE)
        val photoUrl = Firebase.auth.currentUser?.photoUrl.toString()

        Picasso.get().load(photoUrl).transform(CircleTransform()).into(binding.profilepic)

        val displayName = sp.getString("NAME", "NAME")
        val program = sp.getString("PROGRAM", "PROGRAM")
        docId = sp.getString("DOCID", null)!!



        binding.userText.text = displayName
        binding.courseText.text = program

        setupProgress(docId)



        binding.logoutBtn.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, MainActivity:: class.java))
            finish()
        }



        binding.listNav.setOnClickListener (View.OnClickListener {

            val intent = Intent(this, ListsActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            finish()

        })

        binding.calendarNav.setOnClickListener {
            val intent = Intent(this, CalendarActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            finish()
        }

        this.reminderRecycler = binding.recycleReminder
        setupReminderRecycler(docId)


        this.recyclerView2 = binding.recycleTasks
        setupTaskRecycler(docId)

        /**
         * Queries the Firebase Firestore database to check if a remindersList with the given document ID exists.
         * If no remindersList is found, this function creates a new one with the given document ID and
         * default data for an empty remindersList.
         */
        val db = Utility.getCollectionReferenceForReminders(docId)
        val query = db.whereEqualTo("name", "remindersList")
        query.get().addOnSuccessListener { reminder ->
            if(reminder.isEmpty){
                initiateRemindersList(docId)
            }
        }

        binding.profilepic.setOnClickListener {
            val intent = Intent(this, UserProfileActivity::class.java)
            startActivity(intent)
        }

        val toDoOnClick = View.OnClickListener {v: View? ->
            val intent = Intent(this, TasksActivity::class.java)
            intent.putExtra("category", "All Tasks")
            intent.putExtra("mode", "all")
            startActivity(intent)
        }

        val inProgressOnClick = View.OnClickListener {v: View? ->
            val intent = Intent(this, TasksActivity::class.java)
            intent.putExtra("category", "In Progress Tasks")
            intent.putExtra("mode", "inProgress")
            startActivity(intent)
        }

        val completedOnClick = View.OnClickListener {v: View? ->
            val intent = Intent(this, TasksActivity::class.java)
            intent.putExtra("category", "Completed Tasks")
            intent.putExtra("mode", "completed")
            startActivity(intent)
        }

        binding.todopic.setOnClickListener(toDoOnClick)
        binding.todoText.setOnClickListener(toDoOnClick)
        binding.todotasksText.setOnClickListener(toDoOnClick)

        binding.inprogresspic.setOnClickListener(inProgressOnClick)
        binding.inprogressTasks.setOnClickListener(inProgressOnClick)
        binding.inprogressText.setOnClickListener(inProgressOnClick)

        binding.completedpic.setOnClickListener(completedOnClick)
        binding.completedTasks.setOnClickListener(completedOnClick)
        binding.completedText.setOnClickListener(completedOnClick)


    }


    /**
    This function takes a document ID as input and retrieves all tasks associated with that document from Firestore.
    It then calculates the number of tasks that are in progress and the number of tasks that have been completed,
    and updates the corresponding UI elements with the counts.
     **/
     fun setupProgress(docId: String){

        var inProgress = 0
        var completed = 0
        val query = Utility.getCollectionReferenceForAllTasks(docId)

        query.get().addOnSuccessListener { documents ->
            binding.todotasksText.text = "${documents.size()} tasks"
            if(documents.documents.isNotEmpty()){
                for(document in documents.documents){
                    if(document.getBoolean("status") == false){
                        inProgress += 1
                    } else {
                        completed += 1
                    }
                }
            }

            binding.inprogressTasks.text = "$inProgress tasks"
            binding.completedTasks.text = "$completed tasks"

        }
    }

    /**
    This function sets up a FirestoreRecyclerAdapter to display all tasks for a given document ID that have a "todoDate" field equal to today's date.
    It first gets the current date and formats it to match the format used in the "todoDate" field in Firestore.
    It then constructs a query to retrieve all tasks that have a "todoDate" field equal to the current date for the given document ID.
    The query is used to construct a FirestoreRecyclerOptions object, which is used to initialize a TaskAdapter and set it as the adapter for the recyclerView2.
     */
    private fun setupTaskRecycler(docId: String) {
        val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.US)
        val now = Date()
        val formattedDate = dateFormat.format(now)
        val parsedDate = dateFormat.parse(formattedDate)!!
        val timestamp = Timestamp(parsedDate)

        val query = Utility.getCollectionReferenceForAllTasks(docId).whereEqualTo("todoDate", timestamp)
        val options : FirestoreRecyclerOptions<TaskModel> = FirestoreRecyclerOptions.Builder<TaskModel>()
            .setQuery(query, TaskModel::class.java).build()
        recyclerView2.layoutManager = LinearLayoutManager(this)
        taskAdapter = TaskAdapter(options, this, docId, this)
        recyclerView2.adapter = taskAdapter

    }

    /**

    This function sets up a FirestoreRecyclerAdapter to display all reminders for a given document ID that have a "dateTime" field greater than or equal to today's date.
    It first gets the current date and formats it to match the format used in the "dateTime" field in Firestore.
    It then constructs a query to retrieve all reminders that have a "dateTime" field greater than or equal to the current date for the given document ID.
    The query is used to construct a FirestoreRecyclerOptions object, which is used to initialize a ReminderAdapter and set it as the adapter for the reminderRecycler.
    The reminders are ordered in ascending order by the "dateTime" field, so the earliest reminders appear first in the list.
     */
    private fun setupReminderRecycler(docId: String) {

        val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.US)
        val now = Date()
        val formattedDate = dateFormat.format(now)
        val parsedDate = dateFormat.parse(formattedDate)!!
        val timestamp = Timestamp(parsedDate)

        val query = Utility.getCollectionReferenceForReminders(docId)
            .whereGreaterThanOrEqualTo("dateTime", timestamp)
            .orderBy("dateTime", Query.Direction.ASCENDING)
        val options: FirestoreRecyclerOptions<ReminderModel> = FirestoreRecyclerOptions.Builder<ReminderModel>()
            .setQuery(query, ReminderModel::class.java).build()

        reminderRecycler.layoutManager = LinearLayoutManager(this)
        reminderAdapter = ReminderAdapter(options, this, docId)
        reminderRecycler.adapter = reminderAdapter
    }

    private fun initiateRemindersList(docID: String){
        val reminderList = ListModel(" Reminders ", sp.getString("EMAIL", "N/A")!!, Timestamp.now())

        Utility.setList(reminderList, docID)
    }

    override fun onStart() {
        super.onStart()
        taskAdapter.startListening()
        reminderAdapter.startListening()

    }

    override fun onStop() {
        super.onStop()
        taskAdapter.stopListening()
        reminderAdapter.stopListening()

    }

    override fun onResume() {
        super.onResume()
        taskAdapter.notifyDataSetChanged()
        reminderAdapter.notifyDataSetChanged()

    }


}