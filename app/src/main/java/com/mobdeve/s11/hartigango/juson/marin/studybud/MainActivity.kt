package com.mobdeve.s11.hartigango.juson.marin.studybud

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.mobdeve.s11.hartigango.juson.marin.studybud.databinding.ActivityMainBinding
import com.mobdeve.s11.hartigango.juson.marin.studybud.helpers.Utility

class MainActivity : AppCompatActivity() {

    private lateinit var binding:ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var gsc: GoogleSignInClient
    private lateinit var sp: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(this.binding.root)

        auth = FirebaseAuth.getInstance()
        sp = getSharedPreferences("UserInfo", Context.MODE_PRIVATE)

        // Initialize Firebase App
        FirebaseApp.initializeApp(this)

        // Check if user is already signed in
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Set up Google Sign In options
        val webId = getString(R.string.default_web_client)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(webId)
            .requestEmail()
            .build()

        // Sign out from Google
        gsc = GoogleSignIn.getClient(this, gso)
        gsc.signOut()

        binding.btnCont.setOnClickListener {
            signInGoogle()
        }
    }


    private fun signInGoogle() {
        val signInIntent = gsc.signInIntent
        launcher.launch(signInIntent)

    }

    /*

    This block of code registers a callback to be invoked when an activity is started
    for result. If the result code is OK, it gets the signed-in account from the result data
    and calls the handleResults() function passing the task as parameter.
    */
    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result -> if(result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            handleResults(task)
        }
    }


    /*
    This function handles the results of a Google sign-in task.
    It checks if the sign-in was successful and verifies if the email used is from DLSU domain.
    If the sign-in is successful and email is from DLSU, it calls the updateUI function.
    If the email is not from DLSU, it displays a toast message and signs out the user.
    If the sign-in is not successful, it displays a toast message with the exception message.
    */
    private fun handleResults(task: Task<GoogleSignInAccount>) {
        task.addOnCompleteListener { signInTask ->
            if (signInTask.isSuccessful) {
                val account = signInTask.result
                if (account != null) {
                    if(verifyDLSUEmail(account.email)) {
                        updateUI(account)
                    } else {
                        Toast.makeText(this, "Sign-in failed: Must use a DLSU email.", Toast.LENGTH_SHORT).show()
                        auth.signOut()
                        gsc.signOut()
                    }
                }
            } else {
                Toast.makeText(this, "Sign-in failed: ${signInTask.exception}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Updates the UI after a successful Google Sign-In.
     * Retrieves user information from the database and starts the corresponding activity.
     * If the user is not yet registered, directs them to the InfoActivity to complete their profile.
     */
    private fun updateUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener{ task ->
            if(task.isSuccessful){
                val db = FirebaseFirestore.getInstance()
                val users = db.collection("userInfo")
                val query = users.whereEqualTo("email", account.email)
                query.get().addOnSuccessListener { use ->
                    if (use.isEmpty){
                        val intent = Intent(this, InfoActivity::class.java)
                        val editor: SharedPreferences.Editor = sp.edit()
                        editor.putString("EMAIL", account.email)
                        editor.putString("NAME", account.displayName)
                        editor.apply()
                        startActivity(intent)

                    } else {
                        val intent = Intent(this, DashboardActivity::class.java)
                        val userInfo = Utility.getCollectionReferenceForUsers()

                        userInfo.get()
                            .addOnSuccessListener { snapshot ->
                                for(doc in snapshot.documents){
                                    if(doc.data?.get("email") == account.email){
                                        val docId = doc.id
                                        val program = doc.data?.get("program").toString()

                                        val editor: SharedPreferences.Editor = sp.edit()
                                        editor.putString("EMAIL", account.email)
                                        editor.putString("NAME", account.displayName)
                                        editor.putString("DOCID", docId)
                                        editor.putString("PROGRAM", program)
                                        editor.apply()
                                        startActivity(intent)
                                        finish()
                                    }
                                }
                            }

                    }

                }

            } else {
                Toast.makeText(this, task.exception.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun verifyDLSUEmail(str: String?): Boolean { //dlsu.edu.ph
        val n = 11
        var lastChars = str
        if (lastChars != null) {
            lastChars = lastChars.substring(lastChars.length - n, lastChars.length)
        }
        return lastChars == "dlsu.edu.ph"

    }
}