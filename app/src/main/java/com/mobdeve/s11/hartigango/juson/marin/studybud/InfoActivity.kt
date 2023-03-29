package com.mobdeve.s11.hartigango.juson.marin.studybud

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.mobdeve.s11.hartigango.juson.marin.studybud.databinding.InfoScreenBinding

class InfoActivity : AppCompatActivity(){
    private lateinit var binding: InfoScreenBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding = InfoScreenBinding.inflate(layoutInflater)
        setContentView(this.binding.root)

        auth = FirebaseAuth.getInstance()

        val displayName = intent.getStringExtra("name")

        binding.confirmBtn.setOnClickListener {
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)

        }
        binding.welcomeMsg2.setOnClickListener {
            auth.signOut()

            startActivity(Intent(this, MainActivity:: class.java))
        }

        binding.welcomeMsg2.text = "Welcome, ${displayName}"

        binding.editCollege.setOnClickListener{
            binding.editCollege.setText("College of Computer Studies")
        }

        binding.editDegree.setOnClickListener {
            binding.editDegree.setText("BS Information Systems")
        }

        binding.editID.setOnClickListener {
            binding.editID.setText("12000000")
        }
    }
}