package com.mobdeve.s11.hartigango.juson.marin.studybud

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.mobdeve.s11.hartigango.juson.marin.studybud.databinding.ActivityInfoBinding
import com.mobdeve.s11.hartigango.juson.marin.studybud.helpers.Utility
import com.mobdeve.s11.hartigango.juson.marin.studybud.models.UserInfoModel

class InfoActivity : AppCompatActivity(){
    private lateinit var binding: ActivityInfoBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var sp: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding = ActivityInfoBinding.inflate(layoutInflater)
        setContentView(this.binding.root)

        auth = FirebaseAuth.getInstance()
        sp = applicationContext.getSharedPreferences("UserInfo", Context.MODE_PRIVATE)

        val displayName = sp.getString("NAME", "NAME")
        val email = sp.getString("EMAIL", "EMAIL")
      //  val profilePic = intent.getStringExtra("profilepic")
        binding.confirmBtn.setOnClickListener {
            val college = binding.editCollege.text.toString()
            val program = binding.editDegree.text.toString()
            val collegeID = binding.editID.text.toString()
            val name = displayName!!
            val userEmail = email!!

            val user = UserInfoModel(name, userEmail, college, program, collegeID)

            Utility.setUserInfo(user)

            val userInfo = Utility.getCollectionReferenceForUsers()
            userInfo.get().addOnSuccessListener {snapshot ->
                for(doc in snapshot.documents){
                    if(doc.data?.get("email") == userEmail){
                        val docId = doc.id

                        val intent = Intent(this, DashboardActivity::class.java)
                        val editor: SharedPreferences.Editor = sp.edit()
                        editor.putString("PROGRAM", program)
                        editor.putString("DOCID", docId)
                        editor.apply()
                        //   intent.putExtra("profilepic", profilePic)
                        startActivity(intent)
                    }
                }
            }

        }
        binding.welcomeMsg2.setOnClickListener {
            auth.signOut()

            startActivity(Intent(this, MainActivity:: class.java))
        }

        binding.welcomeMsg2.text = "Welcome, ${displayName}"
    }


}