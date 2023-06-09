package com.mobdeve.s11.hartigango.juson.marin.studybud

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.mobdeve.s11.hartigango.juson.marin.studybud.databinding.ActivityInfoBinding
import com.mobdeve.s11.hartigango.juson.marin.studybud.helpers.Utility
import com.mobdeve.s11.hartigango.juson.marin.studybud.models.UserInfoModel

class InfoActivity : AppCompatActivity() {
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

        binding.logoutButton.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }


        /*
        This block handles the onClickListener for the "confirm" button in the user information form. It first checks if all the required fields (college, degree, and ID) are filled. If all fields are filled, it creates a new UserInfoModel object and calls Utility.setUserInfo() to add it to the Firestore database. It then queries the database to find the document with the user's email, retrieves its ID, and saves it to shared preferences along with the user's program. Finally, it starts the DashboardActivity.
        If any of the required fields are empty, it displays a toast message to remind the user to fill all fields.
        */
        binding.confirmBtn.setOnClickListener {

            if (binding.editCollege.text.isNotEmpty() && binding.editDegree.text.isNotEmpty() && binding.editID.text.isNotEmpty()) {
                val college = binding.editCollege.text.toString()
                val program = binding.editDegree.text.toString()
                val collegeID = binding.editID.text.toString()
                val name = displayName!!
                val userEmail = email!!

                val user = UserInfoModel(name, userEmail, college, program, collegeID)

                Utility.setUserInfo(user)

                val userInfo = Utility.getCollectionReferenceForUsers()
                userInfo.get().addOnSuccessListener { snapshot ->
                    for (doc in snapshot.documents) {
                        if (doc.data?.get("email") == userEmail) {
                            val docId = doc.id

                            val intent = Intent(this, DashboardActivity::class.java)
                            val editor: SharedPreferences.Editor = sp.edit()
                            editor.putString("PROGRAM", program)
                            editor.putString("DOCID", docId)
                            editor.apply()

                            startActivity(intent)
                        }
                    }
                }
            } else {
                Toast.makeText(this, "Please fill up ALL fields.", Toast.LENGTH_SHORT).show()
            }


        }
        binding.welcomeMsg2.setOnClickListener {
            auth.signOut()

            startActivity(Intent(this, MainActivity::class.java))
        }

        binding.welcomeMsg2.text = "Welcome, ${displayName}"
    }
}