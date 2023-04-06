package com.mobdeve.s11.hartigango.juson.marin.studybud

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.mobdeve.s11.hartigango.juson.marin.studybud.databinding.ActivityEditTaskBinding
import com.mobdeve.s11.hartigango.juson.marin.studybud.helpers.Utility
import com.mobdeve.s11.hartigango.juson.marin.studybud.models.TaskModel
import java.text.SimpleDateFormat
import java.util.*

class EditTaskActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditTaskBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var sp: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        this.binding = ActivityEditTaskBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(this.binding.root)

        auth = FirebaseAuth.getInstance()
        var date: String = "1/1/2000"
        sp = applicationContext.getSharedPreferences("UserInfo", Context.MODE_PRIVATE)
        val docId = sp.getString("DOCID", "DOCID")!!

        val taskName = intent.getStringExtra("task")!!
        val todoDate = intent.getStringExtra("todoDate")!!
        val status = intent.getBooleanExtra("status", false)
        val notes = intent.getStringExtra("notes")!!
        val category = intent.getStringExtra("category")!!

        binding.tvTaskName.setText(taskName)
        binding.etDateTodo.text = todoDate
        if(status){binding.status.text = "Done"} else {binding.status.text = "In Progress"}
        binding.etCat.text = category
        binding.edNote.setText(notes)

        binding.etDateTodo.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(this, { _, y, m, d ->
                date = "${m+1}/$d/$y"
                binding.etDateTodo.setText(date)
            }, year, month, day)

            datePickerDialog.show()
        }

        this.binding.savebutton.setOnClickListener {
            val query = Utility.getCollectionReferenceForTasks(category, docId)
                .whereEqualTo("category", category)
                .whereEqualTo("task", taskName)
                .limit(1)
            val query2 = Utility.getCollectionReferenceForAllTasks(docId)
                .whereEqualTo("category", category)
                .whereEqualTo("task", taskName)
                .limit(1)

            val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.US)

            val dateTime = dateFormat.parse(date)
            val timestamp = Timestamp(dateTime!!)
            var b = false
            if(binding.status.text == "Done"){
                b = true
            }
            query.get().addOnSuccessListener { documents ->
                if(!documents.isEmpty){
                    val doc = documents.first()
                    val newData = mapOf(
                        "task" to this.binding.tvTaskName.text.toString(),
                        "status" to b,
                        "notes" to this.binding.notes.text.toString(),
                        "category" to this.binding.etCat.text.toString()
                    )

                    doc.reference.update(newData).addOnSuccessListener {
                        Toast.makeText(this, "Task Updated!", Toast.LENGTH_SHORT).show()
                    }

                }

            }

            query2.get().addOnSuccessListener { documents ->
                if(!documents.isEmpty){
                    val doc = documents.first()
                    val newData = mapOf(
                        "task" to this.binding.tvTaskName.text.toString(),
                        "status" to b,
                        "notes" to this.binding.notes.text.toString(),
                        "category" to this.binding.etCat.text.toString()
                    )

                    doc.reference.update(newData)

                }

            }
            finish()
        }

        this.binding.logoutBtn.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, MainActivity:: class.java))
        }


        this.binding.calendarNav.setOnClickListener {
            val intent = Intent(this, CalendarActivity::class.java)
            startActivity(intent)

        }

        this.binding.dashboardNav.setOnClickListener {
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
        }
    }
}