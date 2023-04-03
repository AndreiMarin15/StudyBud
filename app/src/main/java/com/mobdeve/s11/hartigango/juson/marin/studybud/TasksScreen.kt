package com.mobdeve.s11.hartigango.juson.marin.studybud

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mobdeve.s11.hartigango.juson.marin.studybud.databinding.ActivityTasksBinding

class TasksScreen : AppCompatActivity() {
    private lateinit var binding: ActivityTasksBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        this.binding = ActivityTasksBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(this.binding.root)

        binding.imageView12.setOnClickListener{
            val intent = Intent(this, EditTaskActivity:: class.java)
            startActivity(intent)
        }
    }
}