package com.mobdeve.s11.hartigango.juson.marin.studybud

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.Timestamp
import com.mobdeve.s11.hartigango.juson.marin.studybud.databinding.ActivityTaskAddBinding
import com.mobdeve.s11.hartigango.juson.marin.studybud.helpers.Utility
import com.mobdeve.s11.hartigango.juson.marin.studybud.models.ReminderModel
import com.mobdeve.s11.hartigango.juson.marin.studybud.models.TaskModel
import java.text.SimpleDateFormat
import java.util.*


class TaskAddActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTaskAddBinding
    private lateinit var sp: SharedPreferences
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTaskAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val category = intent.getStringExtra("category")!!
        sp = applicationContext.getSharedPreferences("UserInfo", Context.MODE_PRIVATE)
        var date: String = "1/1/2000"
        val docId = sp.getString("DOCID", "DOCID")!!

        binding.etCategory.text = category
        binding.etStatus.text = "Not yet done"

        binding.etTodoDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(this, { _, y, m, d ->
                date = "${m+1}/$d/$y"
                binding.etTodoDate.setText(date)
            }, year, month, day)

            datePickerDialog.show()
        }

        binding.savebutton.setOnClickListener {
            val dateText = binding.etTodoDate.text.toString().trim()

            if(dateText.isNotEmpty() && binding.etTitle.text.isNotEmpty()){

                val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.US)

                val dateTime = dateFormat.parse(date)
                val timestamp = Timestamp(dateTime!!)

                val taskObj = TaskModel(binding.etTitle.text.toString(), timestamp, category, false, binding.etNote.text.toString())

                Utility.setTask(category, taskObj, docId)
                Toast.makeText(this, "Task Added!", Toast.LENGTH_SHORT).show()
                finish()

            } else {
                Toast.makeText(this, "Please fill up all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}