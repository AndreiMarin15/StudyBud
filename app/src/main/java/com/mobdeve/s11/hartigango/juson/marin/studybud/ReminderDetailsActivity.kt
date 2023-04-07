package com.mobdeve.s11.hartigango.juson.marin.studybud

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.mobdeve.s11.hartigango.juson.marin.studybud.databinding.ActivityReminderDetailsBinding
import com.mobdeve.s11.hartigango.juson.marin.studybud.helpers.Utility
import com.mobdeve.s11.hartigango.juson.marin.studybud.models.ReminderModel
import java.text.SimpleDateFormat
import java.util.*

class ReminderDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReminderDetailsBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var sp: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        this.binding = ActivityReminderDetailsBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(this.binding.root)

        auth = FirebaseAuth.getInstance()
        sp = applicationContext.getSharedPreferences("UserInfo", Context.MODE_PRIVATE)
        val docId = sp.getString("DOCID", "DOCID")!!


        val title = intent.getStringExtra("title")!!
        val notes = intent.getStringExtra("notes")!!

        binding.tvTitle.text = title
        binding.inputNotes.setText(notes)

        val remDateTime = intent.getStringExtra("dateTime")!!
        val notifDateTime = intent.getStringExtra("remTime")!!

        val toRegEx = "Timestamp\\(seconds=(\\d+), nanoseconds=(\\d+)\\)".toRegex()


        val rMatchResult = toRegEx.find(remDateTime)
        val rSeconds = rMatchResult!!.groups[1]!!.value.toLong()
        val rNanoSeconds = rMatchResult.groups[2]!!.value.toInt()

        val nMatchResult = toRegEx.find(notifDateTime)
        val nSeconds = nMatchResult!!.groups[1]!!.value.toLong()
        val nNanoSeconds = nMatchResult.groups[2]!!.value.toInt()

        val remDate = Date(rSeconds * 1000)

        val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())

        val sDate = dateFormat.format(remDate)// string for date

        val remTimestamp = Timestamp(rSeconds, rNanoSeconds)

        val timeFormat = SimpleDateFormat("hh:mm:ss a", Locale.getDefault())

        timeFormat.timeZone = TimeZone.getTimeZone("Asia/Manila")

        val sRemTime = timeFormat.format(remTimestamp.toDate()) // string for date and time

        val notifTimestamp = Timestamp(nSeconds, nNanoSeconds)

        val sNotifTime = timeFormat.format(notifTimestamp.toDate())

        binding.editTextDate.text = sDate
        binding.editTextTime.text = sRemTime
        binding.inputRemind.text = sNotifTime

        binding.editTextDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(this, { _, y, m, d ->
                val dateRem = "${m+1}/$d/$y"
                binding.editTextDate.text = dateRem
            }, year, month, day)

            datePickerDialog.show()
        }

        binding.editTextTime.setOnClickListener {
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            val timePickerDialog = TimePickerDialog(this, { _, h, m ->
                val cal = Calendar.getInstance()
                cal.set(Calendar.HOUR_OF_DAY, h)
                cal.set(Calendar.MINUTE, m)
                cal.set(Calendar.SECOND, 0)

                val timeR = timeFormat.format(cal.time)
                binding.editTextTime.text = timeR
            }, hour, minute, false)

            timePickerDialog.show()
        }

        binding.inputRemind.setOnClickListener {
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            val timePickerDialog = TimePickerDialog(this, { _, h, m ->
                val cal = Calendar.getInstance()
                cal.set(Calendar.HOUR_OF_DAY, h)
                cal.set(Calendar.MINUTE, m)
                cal.set(Calendar.SECOND, 0)

                val remind = timeFormat.format(cal.time)
                binding.inputRemind.text = remind
            }, hour, minute, false)

            timePickerDialog.show()
        }


        this.binding.addReminderbtn.setOnClickListener {
            val dateText = binding.editTextDate.text.toString().trim()
            val timeText = binding.editTextTime.text.toString().trim()
            val remText = binding.inputRemind.text.toString().trim()
            val noteText = binding.inputNotes.text.toString().trim()

            val dFormat = SimpleDateFormat("MM/dd/yyyy hh:mm:ss a", Locale.getDefault())

            val dTime = dFormat.parse("$dateText $timeText")

            val remTime = dFormat.parse("$dateText $remText")

            val timestamp = Timestamp(dTime!!)

            val remindstamp = Timestamp(remTime!!)

            val newData = mapOf(
                "dateTime" to timestamp,
                "reminder" to remindstamp,
                "notes" to noteText
            )

            val query = Utility.getCollectionReferenceForReminders(docId)
                .whereEqualTo("title", title)

            query.get().addOnSuccessListener { documents ->
                val doc = documents.first()

                doc.reference.update(newData)
                Toast.makeText(this, "Reminder Updated!", Toast.LENGTH_SHORT).show()

            }
            finish()
        }
    }
}