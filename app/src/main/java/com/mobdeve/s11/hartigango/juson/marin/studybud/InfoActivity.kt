package com.mobdeve.s11.hartigango.juson.marin.studybud

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.mobdeve.s11.hartigango.juson.marin.studybud.databinding.InfoScreenBinding
import com.mobdeve.s11.hartigango.juson.marin.studybud.firestorehelpers.Utility
import com.mobdeve.s11.hartigango.juson.marin.studybud.models.UserInfoModel

class InfoActivity : AppCompatActivity(){
    private lateinit var binding: InfoScreenBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding = InfoScreenBinding.inflate(layoutInflater)
        setContentView(this.binding.root)

        auth = FirebaseAuth.getInstance()

        val displayName = intent.getStringExtra("name")
        val email = intent.getStringExtra("email")
      //  val profilePic = intent.getStringExtra("profilepic")
        binding.confirmBtn.setOnClickListener {
            val college = binding.editCollege.text.toString()
            val program = binding.editDegree.text.toString()
            val collegeID = binding.editID.text.toString()
            val name = displayName!!
            val userEmail = email!!

            val user = UserInfoModel(name, userEmail, college, program, collegeID)

            Utility.setUserInfo(user)

            val intent = Intent(this, DashboardActivity::class.java)
            intent.putExtra("name", displayName)
         //   intent.putExtra("profilepic", profilePic)
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