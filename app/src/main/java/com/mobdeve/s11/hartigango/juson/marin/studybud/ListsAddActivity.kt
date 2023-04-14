package com.mobdeve.s11.hartigango.juson.marin.studybud

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.mobdeve.s11.hartigango.juson.marin.studybud.databinding.ActivityAddTaskBinding
import com.mobdeve.s11.hartigango.juson.marin.studybud.helpers.Utility
import com.mobdeve.s11.hartigango.juson.marin.studybud.models.ListModel

class ListsAddActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddTaskBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var sp: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        this.binding = ActivityAddTaskBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(this.binding.root)

        auth = FirebaseAuth.getInstance()
        sp = applicationContext.getSharedPreferences("UserInfo", Context.MODE_PRIVATE)

        this.binding.createListbtn.setOnClickListener{ // Adds a new list to the database using the information entered by the user
            val name = binding.editListName.text.toString()
            val owner = sp.getString("EMAIL", null)

            val docId = sp.getString("DOCID", null)
            val timestamp = Timestamp.now()

            val list = ListModel(name.trim(), owner!!, timestamp)

            Utility.setList(list,docId!!)

            finish()
        }

        this.binding.calendarNav.setOnClickListener {
            val intent = Intent(this, CalendarActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.dashboardNav.setOnClickListener {
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
            finish()
        }
        this.binding.logoutBtn.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, MainActivity:: class.java))
            finish()
        }
    }
}