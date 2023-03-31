package com.mobdeve.s11.hartigango.juson.marin.studybud


import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s11.hartigango.juson.marin.studybud.databinding.TodaysTasksRecyclerBinding
import com.mobdeve.s11.hartigango.juson.marin.studybud.models.TaskModel

class TaskViewHolder(private val binding: TodaysTasksRecyclerBinding): RecyclerView.ViewHolder(binding.root) {
    fun bindData(taskData: TaskModel){
        binding.task.text = taskData.task
    }
}
