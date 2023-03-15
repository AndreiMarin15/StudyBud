package com.mobdeve.s11.hartigango.juson.marin.studybud

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mobdeve.s11.hartigango.juson.marin.studybud.databinding.InfoScreenBinding

class InfoActivity : AppCompatActivity(){
    private lateinit var binding: InfoScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding = InfoScreenBinding.inflate(layoutInflater)
        setContentView(this.binding.root)

        binding.confirmBtn.setOnClickListener {
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)

        }
    }
}