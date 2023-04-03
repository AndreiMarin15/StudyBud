package com.mobdeve.s11.hartigango.juson.marin.studybud

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mobdeve.s11.hartigango.juson.marin.studybud.databinding.ActivityReminderDetailsBinding

class ReminderDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReminderDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        this.binding = ActivityReminderDetailsBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(this.binding.root)


        this.binding.addReminderbtn.setOnClickListener {
            finish()
        }
    }
}