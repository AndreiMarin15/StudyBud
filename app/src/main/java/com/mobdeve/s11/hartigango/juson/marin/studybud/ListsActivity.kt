package com.mobdeve.s11.hartigango.juson.marin.studybud

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.mobdeve.s11.hartigango.juson.marin.studybud.*
import com.mobdeve.s11.hartigango.juson.marin.studybud.databinding.ActivityListsBinding

class ListsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityListsBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        this.binding = ActivityListsBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(this.binding.root)

        auth = FirebaseAuth.getInstance()

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

        binding.imgBTN1.setOnClickListener{
            val intent = Intent(this, RemindersActivity:: class.java)
            startActivity(intent)
        }

        binding.imgBTN2.setOnClickListener {
            val intent = Intent(this, TasksScreen:: class.java)
            startActivity(intent)
        }


    }

}