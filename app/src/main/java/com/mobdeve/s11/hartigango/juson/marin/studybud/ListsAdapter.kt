package com.mobdeve.s11.hartigango.juson.marin.studybud

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.mobdeve.s11.hartigango.juson.marin.studybud.databinding.LayoutReminderBinding
import com.mobdeve.s11.hartigango.juson.marin.studybud.models.ListModel

class ListsAdapter(options: FirestoreRecyclerOptions<ListModel>, context: Context) : FirestoreRecyclerAdapter<ListModel, ListsAdapter.ListsViewHolder>(options) {

    class ListsViewHolder(val binding: LayoutReminderBinding): RecyclerView.ViewHolder(binding.root) {
        fun bindData(list: ListModel){
            binding.listName.text = list.name
            binding.itemCount.text = "1"

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListsViewHolder {
        val itemViewBinding: LayoutReminderBinding = LayoutReminderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListsViewHolder(itemViewBinding)

        // TODO: Implement itemCount
    }

    override fun onBindViewHolder(holder: ListsViewHolder, position: Int, list: ListModel) {
        holder.bindData(list)
        holder.itemView.setOnClickListener {
            if(holder.binding.listName.text == "Reminders"){
                val intent = Intent(holder.itemView.context, RemindersActivity::class.java)
                holder.itemView.context.startActivity(intent)
            } else {
                val intent = Intent(holder.itemView.context, TasksActivity::class.java)
                holder.itemView.context.startActivity(intent)
            }
        }
    }
}