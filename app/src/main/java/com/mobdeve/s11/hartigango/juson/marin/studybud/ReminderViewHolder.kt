package com.mobdeve.s11.hartigango.juson.marin.studybud

import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s11.hartigango.juson.marin.studybud.databinding.DashboardremindersRecyclerBinding
import com.mobdeve.s11.hartigango.juson.marin.studybud.databinding.ReminderRecyclerBinding

class ReminderViewHolder(private val binding: DashboardremindersRecyclerBinding): RecyclerView.ViewHolder(binding.root) {
    fun bindData(reminderData: ReminderModel){
        this.binding.reminderTitle.text = reminderData.title
        this.binding.reminderTime.text = reminderData.dateTime
    }
}
