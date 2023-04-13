package com.mobdeve.s11.hartigango.juson.marin.studybud

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mobdeve.s11.hartigango.juson.marin.studybud.databinding.ActivityUserProfileBinding
import com.mobdeve.s11.hartigango.juson.marin.studybud.helpers.CircleTransform
import com.mobdeve.s11.hartigango.juson.marin.studybud.helpers.Utility
import com.squareup.picasso.Picasso

class UserProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserProfileBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var sp: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        sp = applicationContext.getSharedPreferences("UserInfo", Context.MODE_PRIVATE)

        val photoUrl = Firebase.auth.currentUser?.photoUrl.toString()

        Picasso.get().load(photoUrl).transform(CircleTransform()).into(binding.profilepic)
        val displayName = sp.getString("NAME", null)
        val email = sp.getString("EMAIL", null)
        val docId = sp.getString("DOCID", null)!!
        binding.tvUserName.text = displayName
        binding.tvEmail.text = email

        val query = Utility.getCollectionReferenceForUsers().document(docId)

        query.get().addOnSuccessListener { user ->
            binding.editCollege.setText(user.getString("college"))
            binding.editDegree.setText(user.getString("program"))
            binding.editID.setText(user.getString("schoolID"))
        }.addOnFailureListener {
            binding.editCollege.setText("college")
            binding.editDegree.setText("program")
            binding.editID.setText("schoolID")
        }

        binding.confirmBtn.setOnClickListener {
            val college = binding.editCollege.text.toString()
            val program = binding.editDegree.text.toString()
            val id = binding.editID.text.toString()

            val newData = mapOf(
                "college" to college,
                "program" to program,
                "schoolID" to id
            )

            query.get().addOnSuccessListener {
                it.reference.update(newData)
                Toast.makeText(this, "User Updated!", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(this, "User Update Failed", Toast.LENGTH_SHORT).show()
            }
        }


    }
}