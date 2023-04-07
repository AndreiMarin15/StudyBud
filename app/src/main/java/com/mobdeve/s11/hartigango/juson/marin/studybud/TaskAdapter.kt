package com.mobdeve.s11.hartigango.juson.marin.studybud

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.mobdeve.s11.hartigango.juson.marin.studybud.databinding.LayoutTodaysTaskBinding
import com.mobdeve.s11.hartigango.juson.marin.studybud.helpers.Utility
import com.mobdeve.s11.hartigango.juson.marin.studybud.models.TaskModel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class TaskAdapter(options: FirestoreRecyclerOptions<TaskModel>, context: Context, docId: String, dbActivity: DashboardActivity?): FirestoreRecyclerAdapter<TaskModel, TaskAdapter.TaskViewHolder>(options) {
    val id = docId
    private val act = dbActivity

    class TaskViewHolder(val binding: LayoutTodaysTaskBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindData(task: TaskModel, id: String) = CoroutineScope(
            Dispatchers.Main).launch {
            binding.task.text = task.task
            binding.task.isChecked = task.status

            var isProcessingClick = false
            binding.task.setOnCheckedChangeListener { buttonView, isChecked ->
                if (!isProcessingClick) {
                    isProcessingClick = true

                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            val newData = mapOf("status" to isChecked)
                            val query = Utility.getCollectionReferenceForTasks(task.category, id)
                                .whereEqualTo("category", task.category)
                                .whereEqualTo("task", task.task)
                                .whereEqualTo("todoDate", task.todoDate)
                                .limit(1)

                            val documents = query.get().await()
                            if (!documents.isEmpty) {
                                val doc = documents.first()
                                Utility.updateTask(task.category, id, doc.id, newData, null).await()
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                            Toast.makeText(binding.root.context, "Error updating task", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                isProcessingClick = false
            }
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
        holder.bindData(task, id)

        holder.binding.btnEdit.setOnClickListener {
            val intent = Intent(holder.itemView.context, EditTaskActivity::class.java)
            intent.putExtra("task", task.task)
            intent.putExtra("todoDate", task.todoDate.toString())
            intent.putExtra("category", task.category)
            intent.putExtra("status", task.status)
            intent.putExtra("notes", task.notes)
            holder.itemView.context.startActivity(intent)
        }

        act?.setupProgress(id)


    }


}
