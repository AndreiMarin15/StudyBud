package com.mobdeve.s11.hartigango.juson.marin.studybud

import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s11.hartigango.juson.marin.studybud.databinding.LayoutDashboardRemindersBinding
import com.mobdeve.s11.hartigango.juson.marin.studybud.models.ReminderModel

class ReminderViewHolder(private val binding: LayoutDashboardRemindersBinding): RecyclerView.ViewHolder(binding.root) {
    fun bindData(reminderData: ReminderModel){
        this.binding.reminderTitle.text = reminderData.title
        this.binding.reminderTime.text = reminderData.dateTime
    }
}
