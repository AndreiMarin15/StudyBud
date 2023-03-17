package com.mobdeve.s11.hartigango.juson.marin.studybud

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mobdeve.s11.hartigango.juson.marin.studybud.databinding.ActivityEditTaskBinding

class EditTaskActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditTaskBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        this.binding = ActivityEditTaskBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(this.binding.root)

        this.binding.savebutton.setOnClickListener {
            finish()
        }
    }
}