package com.mobdeve.s11.hartigango.juson.marin.studybud

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.mobdeve.s11.hartigango.juson.marin.studybud.databinding.ActivityRemindersBinding

class RemindersActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRemindersBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        this.binding = ActivityRemindersBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(this.binding.root)


        binding.first.setOnClickListener{
            val intent = Intent(this, ReminderDetailsActivity::class.java)

            startActivity(intent)
        }

        binding.second.setOnClickListener{
            val intent = Intent(this, ReminderDetailsActivity::class.java)

            startActivity(intent)
        }

        binding.third.setOnClickListener{
            val intent = Intent(this, ReminderDetailsActivity::class.java)

            startActivity(intent)
        }

        binding.addReminderbtn.setOnClickListener{
            val intent = Intent(this, ReminderAddActivity::class.java)

            startActivity(intent)
        }
    }
}