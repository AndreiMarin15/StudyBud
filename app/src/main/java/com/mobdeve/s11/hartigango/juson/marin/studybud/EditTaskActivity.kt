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
        var date: String
        sp = applicationContext.getSharedPreferences("UserInfo", Context.MODE_PRIVATE)
        val docId = sp.getString("DOCID", null)!!

        val taskName = intent.getStringExtra("task")!!

        var status = intent.getBooleanExtra("status", false)
        val notes = intent.getStringExtra("notes")!!
        val category = intent.getStringExtra("category")!!

        binding.tvTaskName.setText(taskName)




        val todoDate = intent.getStringExtra("todoDate")!!

        val todoDateRegEx = "Timestamp\\(seconds=(\\d+), nanoseconds=(\\d+)\\)".toRegex()

        val matchResult = todoDateRegEx.find(todoDate)
        val seconds = matchResult!!.groups[1]!!.value.toLong()
        val nanoseconds = matchResult.groups[2]!!.value.toInt()

        val newDate = Date(seconds * 1000)

        val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.US)

        val sDate = dateFormat.format(newDate)

        val tstamp = Timestamp(seconds, nanoseconds)

        val formattedStamp = SimpleDateFormat("MMMM d, yyyy 'at' h:mm:ss a 'UTC'Z", Locale.US)

        formattedStamp.timeZone = TimeZone.getTimeZone("UTC+8")

        val sStamp = formattedStamp.format(tstamp.toDate())


        binding.etDateTodo.text = sDate

        if(status){
            binding.status.text = "Done"
            binding.imgStatus.setImageResource(R.drawable.completed)
        } else {
            binding.status.text = "In Progress"
            binding.imgStatus.setImageResource(R.drawable.inprogress)
        }

        binding.imgStatus.setOnClickListener {
            if (!status){
                binding.status.text = "Done"
                binding.imgStatus.setImageResource(R.drawable.completed)
                status = true
            } else {
                binding.status.text = "In Progress"
                binding.imgStatus.setImageResource(R.drawable.inprogress)
                status = false
            }
        }
        binding.etCat.text = category
        binding.edNote.setText(notes)

        binding.etDateTodo.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(this, { _, y, m, d ->
                date = "${m+1}/$d/$y"
                binding.etDateTodo.text = date
            }, year, month, day)

            datePickerDialog.show()
        }

        this.binding.savebutton.setOnClickListener {
            val query = Utility.getCollectionReferenceForTasks(category, docId)
                .whereEqualTo("category", category)
                .whereEqualTo("task", taskName)
                .limit(1)

            var b = false
            if(binding.status.text == "Done"){
                b = true
            }

            query.get().addOnSuccessListener { documents ->
                if(!documents.isEmpty){
                    val doc = documents.first()

                    if(this.binding.etDateTodo.text.toString() == sStamp){
                        val newData = mapOf(
                            "task" to this.binding.tvTaskName.text.toString(),
                            "status" to b,
                            "notes" to this.binding.edNote.text.toString(),
                            "category" to this.binding.etCat.text.toString(),
                            "todoDate" to sStamp
                        )

                        Utility.updateTask(this.binding.etCat.text.toString(), docId, doc.id, null, newData)
                        Toast.makeText(this, "Task Updated!", Toast.LENGTH_SHORT).show()
                    } else {

                        val dateTime = dateFormat.parse(sDate)
                        val timestamp = Timestamp(dateTime!!)

                        val newData = mapOf(
                            "task" to this.binding.tvTaskName.text.toString(),
                            "status" to b,
                            "notes" to this.binding.edNote.text.toString(),
                            "category" to this.binding.etCat.text.toString(),
                            "todoDate" to timestamp
                        )

                        Utility.updateTask(this.binding.etCat.text.toString(), docId, doc.id, null, newData)
                        Toast.makeText(this, "Task Updated!", Toast.LENGTH_SHORT).show()
                    }
                }

            }
            finish()
        }

        this.binding.logoutBtn.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, MainActivity:: class.java))
            finish()
        }


        this.binding.calendarNav.setOnClickListener {
            val intent = Intent(this, CalendarActivity::class.java)
            startActivity(intent)
            finish()

        }

        this.binding.dashboardNav.setOnClickListener {
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}