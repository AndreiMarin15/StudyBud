package com.mobdeve.s11.hartigango.juson.marin.studybud

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.mobdeve.s11.hartigango.juson.marin.studybud.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding:ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var gsc: GoogleSignInClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(this.binding.root)

        auth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("136493390124-rjm4gqad82l7q78n8098cfhrs6861v2r.apps.googleusercontent.com")
            .requestEmail()
            .build()
        // "136493390124-rjm4gqad82l7q78n8098cfhrs6861v2r.apps.googleusercontent.com"
        // getString(R.string.default_web_client_id)
        gsc = GoogleSignIn.getClient(this, gso)
        gsc.signOut()

        binding.btnCont.setOnClickListener(View.OnClickListener {
        //val intent = Intent(this, InfoActivity::class.java)
        //startActivity(intent)

            signInGoogle()
        })

    }

    private fun signInGoogle() {
        val signInIntent = gsc.signInIntent
        launcher.launch(signInIntent)

    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){

        result -> if(result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                handleResults(task)
                 }
    }

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

    private fun updateUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener{
            if(it.isSuccessful){
                val intent = Intent(this, InfoActivity::class.java)
                intent.putExtra("email", account.email)
                intent.putExtra("name", account.displayName)
                startActivity(intent)
            } else {
                Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun verifyDLSUEmail(str: String?): Boolean { //dlsu.edu.ph
        val n: Int = 11
        var lastChars = str
        if (lastChars != null) {
            lastChars = lastChars.substring(lastChars.length - n, lastChars.length)
        }
        return lastChars == "dlsu.edu.ph"

    }
}