package com.mobdeve.s11.hartigango.juson.marin.studybud

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StrikethroughSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.Timestamp
import com.mobdeve.s11.hartigango.juson.marin.studybud.databinding.LayoutDashboardRemindersBinding
import com.mobdeve.s11.hartigango.juson.marin.studybud.databinding.LayoutTodaysTaskBinding
import com.mobdeve.s11.hartigango.juson.marin.studybud.helpers.Utility
import com.mobdeve.s11.hartigango.juson.marin.studybud.models.ReminderModel
import com.mobdeve.s11.hartigango.juson.marin.studybud.models.TaskModel

class TaskAdapter(options: FirestoreRecyclerOptions<TaskModel>, context: Context, docId: String, dbActivity: DashboardActivity?): FirestoreRecyclerAdapter<TaskModel, TaskAdapter.TaskViewHolder>(options) {
    val id = docId
    val act = dbActivity

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

        holder.binding.task.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {


                val query = Utility.getCollectionReferenceForTasks(task.category, id)
                    .whereEqualTo("category", task.category)
                    .whereEqualTo("task", task.task)
                    .whereEqualTo("todoDate", task.todoDate)
                    .limit(1)
                val query2 = Utility.getCollectionReferenceForAllTasks(id)
                    .whereEqualTo("category", task.category)
                    .whereEqualTo("task", task.task)
                    .whereEqualTo("todoDate", task.todoDate)
                    .limit(1)

                query.get().addOnSuccessListener { documents ->
                    if(!documents.isEmpty){
                        val doc = documents.first()
                        val newData = mapOf(
                            "status" to true
                        )

                        doc.reference.update(newData)

                    }

                }

                query2.get().addOnSuccessListener { documents ->
                    if(!documents.isEmpty){
                        val doc = documents.first()
                        val newData = mapOf(
                            "status" to true
                        )

                        doc.reference.update(newData)

                    }

                }

            } else {

                val query = Utility.getCollectionReferenceForTasks(task.category, id)
                    .whereEqualTo("category", task.category)
                    .whereEqualTo("task", task.task)
                    .whereEqualTo("todoDate", task.todoDate)
                    .limit(1)
                val query2 = Utility.getCollectionReferenceForAllTasks(id)
                    .whereEqualTo("category", task.category)
                    .whereEqualTo("task", task.task)
                    .whereEqualTo("todoDate", task.todoDate)
                    .limit(1)

                query.get().addOnSuccessListener { documents ->
                    if(!documents.isEmpty){
                        val doc = documents.first()
                        val newData = mapOf(
                            "status" to false
                        )

                        doc.reference.update(newData)

                    }

                }

                query2.get().addOnSuccessListener { documents ->
                    if(!documents.isEmpty){
                        val doc = documents.first()
                        val newData = mapOf(
                            "status" to false
                        )

                        doc.reference.update(newData)

                    }

                }
            }


        }

        act?.setupProgress(id)
    }


}
