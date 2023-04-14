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
import okhttp3.internal.Util

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

        holder.binding.todotask.setOnClickListener {
            val intent = Intent(holder.itemView.context, EditTaskActivity::class.java)
            intent.putExtra("task", task.task)
            intent.putExtra("todoDate", task.todoDate.toString())
            intent.putExtra("category", task.category)
            intent.putExtra("status", task.status)
            intent.putExtra("notes", task.notes)
            holder.itemView.context.startActivity(intent)
        }

        act?.setupProgress(id)


        /*
        This code sets an OnClickListener on a button in a RecyclerView item. When clicked, it retrieves a Firestore document that matches
        the task's category, task name, and todo date. If a matching document is found, it is deleted using a Utility method that takes the category, document ID, and task ID as arguments.
        If the deletion is successful, a "Task Deleted" message is displayed to the user using a Toast.
        */
        holder.binding.btnDelete.setOnClickListener {
            Utility.getCollectionReferenceForTasks(task.category, id)
                .whereEqualTo("category", task.category)
                .whereEqualTo("task", task.task)
                .whereEqualTo("todoDate", task.todoDate)
                .limit(1)
                .get()
                .addOnSuccessListener {document ->
                    if(!document.isEmpty){
                        val doc = document.first()

                        Utility.deleteTask(task.category, id, doc.id)

                        Toast.makeText(holder.itemView.context, "Task Deleted", Toast.LENGTH_SHORT).show()
                    }
                }
        }

    }


}
