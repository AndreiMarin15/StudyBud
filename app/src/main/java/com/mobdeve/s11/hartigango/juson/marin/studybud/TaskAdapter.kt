package com.mobdeve.s11.hartigango.juson.marin.studybud

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.mobdeve.s11.hartigango.juson.marin.studybud.databinding.LayoutDashboardRemindersBinding
import com.mobdeve.s11.hartigango.juson.marin.studybud.databinding.LayoutTodaysTaskBinding
import com.mobdeve.s11.hartigango.juson.marin.studybud.models.ReminderModel
import com.mobdeve.s11.hartigango.juson.marin.studybud.models.TaskModel

class TaskAdapter(options: FirestoreRecyclerOptions<TaskModel>, context: Context): FirestoreRecyclerAdapter<TaskModel, TaskAdapter.TaskViewHolder>(options) {
    class TaskViewHolder(val binding: LayoutTodaysTaskBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindData(task: TaskModel){
            binding.task.text = task.task
            binding.task.isChecked = task.status
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val itemViewBinding: LayoutTodaysTaskBinding = LayoutTodaysTaskBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return TaskViewHolder(itemViewBinding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int, task: TaskModel) {
        holder.bindData(task)

        holder.binding.btnEdit.setOnClickListener {
            val intent = Intent(holder.itemView.context, EditTaskActivity::class.java)
            intent.putExtra("task", task.task)
            intent.putExtra("todoDate", task.todoDate.toString())
            intent.putExtra("category", task.category)
            intent.putExtra("status", task.status)
            intent.putExtra("notes", task.notes)
            holder.itemView.context.startActivity(intent)
        }
    }


}
