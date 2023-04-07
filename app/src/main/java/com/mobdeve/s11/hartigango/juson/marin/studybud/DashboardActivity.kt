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
    private lateinit var reminderData: ArrayList<ReminderModel>
    private lateinit var reminderAdapter: ReminderAdapter
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var taskData: ArrayList<TaskModel>
    private lateinit var reminderRecycler: RecyclerView
    private lateinit var recyclerView2: RecyclerView
    private lateinit var binding: ActivityDashboardBinding
    private lateinit var docId: String
    private lateinit var auth: FirebaseAuth
    private lateinit var sp: SharedPreferences

    private val viewNoteLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result: ActivityResult -> if(result.resultCode == RESULT_OK){
        this.reminderAdapter.notifyItemChanged(0)
    }
    }

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
        docId = sp.getString("DOCID", "DOCID")!!

        // val profilepic = intent.getStringExtra("profilepic")

        binding.userText.text = displayName
        binding.courseText.text = program

        setupProgress(docId)

        // binding.profilepic.setImageURI(Uri.parse(profilepic))

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

        this.reminderData = ReminderHelper.initializeData()
        this.taskData = TaskHelper.initializeData()

        this.reminderRecycler = binding.recycleReminder
        setupReminderRecycler(docId)


        this.recyclerView2 = binding.recycleTasks
        setupTaskRecycler(docId)


        val db = Utility.getCollectionReferenceForReminders(docId)
        val query = db.whereEqualTo("name", "remindersList")
        query.get().addOnSuccessListener { reminder ->
            if(reminder.isEmpty){
                initiateRemindersList(docId)
            }
        }


    }

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


    private fun setupReminderRecycler(docId: String) {
        val query = Utility.getCollectionReferenceForReminders(docId).orderBy("dateTime", Query.Direction.ASCENDING)
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