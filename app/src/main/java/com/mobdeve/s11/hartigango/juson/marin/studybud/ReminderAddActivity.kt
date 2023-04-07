package com.mobdeve.s11.hartigango.juson.marin.studybud


import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import com.google.firebase.Timestamp
import com.mobdeve.s11.hartigango.juson.marin.studybud.databinding.ActivityReminderAddBinding
import com.mobdeve.s11.hartigango.juson.marin.studybud.helpers.Utility
import com.mobdeve.s11.hartigango.juson.marin.studybud.models.ReminderModel
import java.text.SimpleDateFormat
import java.util.*


class ReminderAddActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReminderAddBinding
    private lateinit var sp: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityReminderAddBinding.inflate(layoutInflater)

        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        sp = applicationContext.getSharedPreferences("UserInfo", Context.MODE_PRIVATE)

        var date: String = "1/1/2000"
        var time: String = "00:00"
        var remind: String = "00:00"



        binding.etDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(this, { _, y, m, d ->
                date = "${m+1}/$d/$y"
                binding.etDate.setText(date)
            }, year, month, day)

            datePickerDialog.show()
        }

        binding.etTime.setOnClickListener {
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            val timePickerDialog = TimePickerDialog(this, { _, h, m ->
                val cal = Calendar.getInstance()
                cal.set(Calendar.HOUR_OF_DAY, h)
                cal.set(Calendar.MINUTE, m)
                cal.set(Calendar.SECOND, 0)

                val timeFormat = SimpleDateFormat("hh:mm:ss a", Locale.getDefault())

                time = timeFormat.format(cal.time)
                binding.etTime.setText(time)
            }, hour, minute, false)

            timePickerDialog.show()
        }

        binding.etRemind.setOnClickListener {
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            val timePickerDialog = TimePickerDialog(this, { _, h, m ->
                val cal = Calendar.getInstance()
                cal.set(Calendar.HOUR_OF_DAY, h)
                cal.set(Calendar.MINUTE, m)
                cal.set(Calendar.SECOND, 0)

                val timeFormat = SimpleDateFormat("hh:mm:ss a", Locale.getDefault())

                remind = timeFormat.format(cal.time)
                binding.etRemind.setText(remind)
            }, hour, minute, false)

            timePickerDialog.show()
        }

        binding.btnSave.setOnClickListener {
            val dateText = binding.etDate.text.toString().trim()
            val nameText = binding.etName.text.toString().trim()
            val timeText = binding.etTime.text.toString().trim()
            val remText = binding.etRemind.text.toString().trim()

            if(dateText.isNotEmpty() && nameText.isNotEmpty() && timeText.isNotEmpty() && remText.isNotEmpty()){

                val dateFormat = SimpleDateFormat("MM/dd/yyyy hh:mm:ss a", Locale.getDefault())

                val dateTime = dateFormat.parse("$date $time")

                val remTime = dateFormat.parse("$date $remind")

                val timestamp = Timestamp(dateTime!!)

                val remindstamp = Timestamp(remTime!!)

                val reminderObj = ReminderModel(binding.etName.text.toString(), timestamp, remindstamp, binding.etNotes.text.toString())

                Utility.setReminder(reminderObj, sp.getString("DOCID", "N/A")!!)
                Toast.makeText(this, "Reminder Added!", Toast.LENGTH_SHORT).show()
                finish()

            } else {
                Toast.makeText(this, "Please fill up all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}