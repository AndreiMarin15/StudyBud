package com.mobdeve.s11.hartigango.juson.marin.studybud

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mobdeve.s11.hartigango.juson.marin.studybud.databinding.ActivityCalendarBinding
import com.mobdeve.s11.hartigango.juson.marin.studybud.databinding.ListsScreenBinding

class CalendarActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCalendarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        this.binding = ActivityCalendarBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(this.binding.root)

        binding.dashboardNav.setOnClickListener {
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
        }

        binding.listNav.setOnClickListener {
            val intent = Intent(this, ListsActivity:: class.java)
            startActivity(intent)
        }
    }
}