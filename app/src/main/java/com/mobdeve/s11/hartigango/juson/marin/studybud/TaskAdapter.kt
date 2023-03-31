package com.mobdeve.s11.hartigango.juson.marin.studybud

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s11.hartigango.juson.marin.studybud.databinding.TodaysTasksRecyclerBinding
import com.mobdeve.s11.hartigango.juson.marin.studybud.models.TaskModel

class TaskAdapter(private val taskData: ArrayList<TaskModel>): RecyclerView.Adapter<TaskViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val itemViewBinding: TodaysTasksRecyclerBinding = TodaysTasksRecyclerBinding.inflate(
            LayoutInflater.from(parent.context),
             parent,
            false)

        return TaskViewHolder(itemViewBinding)
    }

    override fun getItemCount(): Int {
        return taskData.size
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bindData(taskData[position])
    }

}
