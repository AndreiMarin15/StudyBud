package com.mobdeve.s11.hartigango.juson.marin.studybud

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
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

class DashboardActivity: AppCompatActivity() {
    private lateinit var reminderData: ArrayList<ReminderModel>
    private lateinit var reminderAdapter: ReminderAdapter
    private lateinit var taskData: ArrayList<TaskModel>
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var reminderRecycler: RecyclerView
    private lateinit var recyclerView2: RecyclerView
    private lateinit var binding: ActivityDashboardBinding

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
        val docId = sp.getString("DOCID", "DOCID")

       // val profilepic = intent.getStringExtra("profilepic")

        binding.userText.text = displayName
        binding.courseText.text = program

        // binding.profilepic.setImageURI(Uri.parse(profilepic))

        binding.logoutBtn.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, MainActivity:: class.java))
            finish()
        }

        binding.listNav.setOnClickListener (View.OnClickListener {

            val intent = Intent(this, ListsActivity::class.java)
            startActivity(intent)

        })

        binding.calendarNav.setOnClickListener {
            val intent = Intent(this, CalendarActivity::class.java)
            startActivity(intent)

        }

        this.reminderData = ReminderHelper.initializeData()
        this.taskData = TaskHelper.initializeData()

        this.reminderRecycler = binding.recycleReminder
        setupReminderRecycler(docId!!)

        this.recyclerView2 = binding.recycleTasks
        this.taskAdapter = TaskAdapter(taskData)
        this.recyclerView2.adapter = taskAdapter
        this.recyclerView2.layoutManager = LinearLayoutManager(this)

        val db = Utility.getCollectionReferenceForLists(docId)
        val query = db.whereEqualTo("name", "remindersList").orderBy( "dateTime", Query.Direction.ASCENDING)
        query.get().addOnSuccessListener { reminder ->
            if(reminder.isEmpty){
                initiateRemindersList(docId)
            }
        }


    }

    private fun setupReminderRecycler(docId: String) {
        val query = Utility.getCollectionReferenceForReminders(docId)
        val options: FirestoreRecyclerOptions<ReminderModel> = FirestoreRecyclerOptions.Builder<ReminderModel>()
            .setQuery(query, ReminderModel::class.java).build()

        reminderRecycler.layoutManager = LinearLayoutManager(this)
        reminderAdapter = ReminderAdapter(options, this)
        reminderRecycler.adapter = reminderAdapter
    }

    private fun initiateRemindersList(docID: String){
        val reminderList = ListModel("Reminders", sp.getString("EMAIL", "N/A")!!, Timestamp.now())

        Utility.setList(reminderList, docID)
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