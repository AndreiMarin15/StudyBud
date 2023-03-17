package com.mobdeve.s11.hartigango.juson.marin.studybud

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.mobdeve.s11.hartigango.juson.marin.studybud.databinding.AddtaskScreenBinding

class AddTaskActivity : AppCompatActivity() {
    private lateinit var binding: AddtaskScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        this.binding = AddtaskScreenBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(this.binding.root)

        binding.createListbtn.setOnClickListener{
            finish()
        }
    }
}