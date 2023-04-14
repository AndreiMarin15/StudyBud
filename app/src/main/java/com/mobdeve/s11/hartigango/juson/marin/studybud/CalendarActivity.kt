package com.mobdeve.s11.hartigango.juson.marin.studybud

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query
import com.mobdeve.s11.hartigango.juson.marin.studybud.databinding.ActivityCalendarBinding
import com.mobdeve.s11.hartigango.juson.marin.studybud.ListsActivity
import com.mobdeve.s11.hartigango.juson.marin.studybud.helpers.Utility
import com.mobdeve.s11.hartigango.juson.marin.studybud.models.ReminderModel
import com.mobdeve.s11.hartigango.juson.marin.studybud.models.TaskModel
import java.text.SimpleDateFormat
import java.util.*

class CalendarActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCalendarBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var reminderRecycler: RecyclerView
    private lateinit var reminderAdapter: ReminderAdapter
    private lateinit var taskRecycler: RecyclerView
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var sp:SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        this.binding = ActivityCalendarBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(this.binding.root)

        binding.logoutBtn.setOnClickListener {// logs out the current auth instance or the user
            auth.signOut()
            startActivity(Intent(this, MainActivity:: class.java))
            finish()
        }

        binding.dashboardNav.setOnClickListener {// go to the dashboard activity
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
            finish()
        }

        binding.listNav.setOnClickListener { // go to the lists activity
            val intent = Intent(this, ListsActivity:: class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
            finish()
        }


        auth = FirebaseAuth.getInstance()

        sp = applicationContext.getSharedPreferences("UserInfo", Context.MODE_PRIVATE)
        val docId = sp.getString("DOCID", null)!!

        val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.US)


        val selectedDate = binding.calendarView.date
        val selectedDateString = dateFormat.format(Date(selectedDate))
        val selectedDateObject = dateFormat.parse(selectedDateString)
        val timestamp = Timestamp(selectedDateObject!!
        )

        reminderRecycler = binding.remRecycle
        setupReminderRecycler(docId, timestamp)

        taskRecycler = binding.taskRecycle
        setupTaskRecycler(docId, timestamp)

        // This code sets a listener for when the user selects a date in a calendar view.
        // When a date is selected, it creates a calendar instance and sets its date to the selected year, month, and day.
        // It then converts the calendar's date to a Date object, formats the date as a string using a date formatter,
        // parses the formatted date string back into a Date object using the same formatter, and converts the Date object
        // to a Timestamp object. Finally, it sets up two recycler views to display tasks and reminders for the selected date.
        binding.calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->

            val calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth)
            val sDate = calendar.time

            val fDate = dateFormat.format(sDate)

            val pDate = dateFormat.parse(fDate)!!
            val tstamp = Timestamp(pDate)

            setupTaskRecycler(docId, tstamp)

            setupReminderRecycler(docId, tstamp)

        }

    }

    /**
     * Sets up a recycler view to display reminders for a given document ID and timestamp.
     * This function first creates a calendar instance and sets its date to the given timestamp's date.
     * It then adds one day to the calendar's date to get the next day's date and creates a new timestamp
     * for that date. Using these timestamps, it queries the Firebase Firestore database to get all reminders
     * that fall between the given timestamp and the next day's timestamp, ordered by their date and time in
     * ascending order. It then creates a FirestoreRecyclerOptions object with the query and ReminderModel class,
     * sets up a reminder adapter with the options, and sets the adapter to the recycler view. Finally, it starts
     * listening for changes to the adapter and notifies the adapter that its data has changed.
     */
    private fun setupReminderRecycler(docId: String, timestamp: Timestamp) {
        val calendar = Calendar.getInstance()
        calendar.time = timestamp.toDate()
        calendar.add(Calendar.DAY_OF_MONTH, 1)
        val updatedDate = calendar.time
        val updatedTimestamp = Timestamp(updatedDate)


        val query = Utility.getCollectionReferenceForReminders(docId)
            .whereGreaterThanOrEqualTo("dateTime", timestamp)
            .whereLessThan("dateTime", updatedTimestamp)
            .orderBy("dateTime", Query.Direction.ASCENDING)


        val options: FirestoreRecyclerOptions<ReminderModel> = FirestoreRecyclerOptions.Builder<ReminderModel>()
            .setQuery(query, ReminderModel::class.java).build()


        reminderRecycler.layoutManager = LinearLayoutManager(this)
        reminderAdapter = ReminderAdapter(options, this, docId)
        reminderRecycler.adapter = reminderAdapter

        reminderAdapter.updateOptions(options)
        reminderAdapter.startListening()
        reminderAdapter.notifyDataSetChanged()
    }

    /**
     * Sets up a recycler view to display tasks for a given document ID and timestamp.
     * This function queries the Firebase Firestore database to get all tasks that have a todoDate equal
     * to the given timestamp. It then creates a FirestoreRecyclerOptions object with the query and TaskModel class,
     * sets up a task adapter with the options, and sets the adapter to the recycler view. Finally, it starts
     * listening for changes to the adapter and notifies the adapter that its data has changed.
     */
    private fun setupTaskRecycler(docId: String, timestamp: Timestamp) {
        val query = Utility.getCollectionReferenceForAllTasks(docId)
            .whereEqualTo("todoDate", timestamp)

        val options : FirestoreRecyclerOptions<TaskModel> = FirestoreRecyclerOptions.Builder<TaskModel>()
            .setQuery(query, TaskModel::class.java).build()


        taskRecycler.layoutManager = LinearLayoutManager(this)
        taskAdapter = TaskAdapter(options, this, docId, null)
        taskRecycler.adapter = taskAdapter

        taskAdapter.updateOptions(options)
        taskAdapter.startListening()
        taskAdapter.notifyDataSetChanged()
    }

    override fun onStart() {
        super.onStart()
        taskAdapter.startListening()
        reminderAdapter.startListening()

    }

    override fun onStop() {
        super.onStop()
        taskAdapter.stopListening()
        reminderAdapter.stopListening()

    }

    override fun onResume() {
        super.onResume()
        taskAdapter.notifyDataSetChanged()
        reminderAdapter.notifyDataSetChanged()

    }
}