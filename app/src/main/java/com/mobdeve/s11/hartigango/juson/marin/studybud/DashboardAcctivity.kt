package com.mobdeve.s11.hartigango.juson.marin.studybud

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.mobdeve.s11.hartigango.juson.marin.studybud.databinding.DashboardScreenBinding
import com.mobdeve.s11.hartigango.juson.marin.studybud.models.ReminderModel
import com.mobdeve.s11.hartigango.juson.marin.studybud.models.TaskModel

class DashboardActivity: AppCompatActivity() {
    private lateinit var reminderData: ArrayList<ReminderModel>
    private lateinit var reminderAdapter: ReminderAdapter
    private lateinit var taskData: ArrayList<TaskModel>
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerView2: RecyclerView
    private lateinit var binding: DashboardScreenBinding
    private lateinit var auth: FirebaseAuth

    private val viewNoteLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        result: ActivityResult -> if(result.resultCode == RESULT_OK){
            this.reminderAdapter.notifyItemChanged(0)
    }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding = DashboardScreenBinding.inflate(layoutInflater)
        setContentView(this.binding.root)

        auth = FirebaseAuth.getInstance()
        val displayName = intent.getStringExtra("name")
       // val profilepic = intent.getStringExtra("profilepic")
        binding.userText.text = "${displayName}"
       // binding.profilepic.setImageURI(Uri.parse(profilepic))

        binding.logoutBtn.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, MainActivity:: class.java))
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

        this.recyclerView = binding.recycleReminder
        this.reminderAdapter = ReminderAdapter(reminderData, viewNoteLauncher)
        this.recyclerView.adapter = reminderAdapter
        this.recyclerView.layoutManager = LinearLayoutManager(this)

        this.recyclerView2 = binding.recycleTasks
        this.taskAdapter = TaskAdapter(taskData)
        this.recyclerView2.adapter = taskAdapter
        this.recyclerView2.layoutManager = LinearLayoutManager(this)

    }
}