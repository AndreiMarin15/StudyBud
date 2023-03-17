package com.mobdeve.s11.hartigango.juson.marin.studybud

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s11.hartigango.juson.marin.studybud.databinding.DashboardScreenBinding
import com.mobdeve.s11.hartigango.juson.marin.studybud.databinding.InfoScreenBinding

class DashboardActivity: AppCompatActivity() {
    private lateinit var reminderData: ArrayList<ReminderModel>
    private lateinit var reminderAdapter: ReminderAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var binding: DashboardScreenBinding
    private val viewNoteLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        result: ActivityResult -> if(result.resultCode == RESULT_OK){
            this.reminderAdapter.notifyItemChanged(0)
    }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding = DashboardScreenBinding.inflate(layoutInflater)
        setContentView(this.binding.root)


        binding.listNav.setOnClickListener (View.OnClickListener {

            val intent = Intent(this, ListsActivity::class.java)
            startActivity(intent)
        })

                binding.calendarNav.setOnClickListener {
            val intent = Intent(this, CalendarActivity::class.java)
            startActivity(intent)
        }

        this.reminderData = ReminderHelper.initializeData()

        this.recyclerView = binding.recycleReminder
        this.reminderAdapter = ReminderAdapter(reminderData, viewNoteLauncher)
        this.recyclerView.adapter = reminderAdapter
        this.recyclerView.layoutManager = LinearLayoutManager(this)


    }
}