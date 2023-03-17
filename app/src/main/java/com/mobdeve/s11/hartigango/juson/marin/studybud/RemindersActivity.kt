package com.mobdeve.s11.hartigango.juson.marin.studybud

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mobdeve.s11.hartigango.juson.marin.studybud.databinding.RemindersScreenBinding

class RemindersActivity : AppCompatActivity() {
    private lateinit var binding: RemindersScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        this.binding = RemindersScreenBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(this.binding.root)

        binding.addReminderbtn.setOnClickListener{
            val intent = Intent(this, ReminderDetailsActivity::class.java)
            startActivity(intent)
        }
    }
}