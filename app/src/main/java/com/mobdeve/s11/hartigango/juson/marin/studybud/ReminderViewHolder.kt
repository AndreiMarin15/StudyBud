package com.mobdeve.s11.hartigango.juson.marin.studybud

import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s11.hartigango.juson.marin.studybud.databinding.ReminderRecyclerBinding

class ReminderViewHolder(private val binding: ReminderRecyclerBinding): RecyclerView.ViewHolder(binding.root) {
    fun bindData(reminderData: ReminderModel){
        this.binding.listName.text = reminderData.title
        this.binding.itemCount.text = reminderData.dateTime
    }
}
