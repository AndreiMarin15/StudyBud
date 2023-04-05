package com.mobdeve.s11.hartigango.juson.marin.studybud

import android.content.Context
import android.content.Intent
import android.view.ViewGroup
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.mobdeve.s11.hartigango.juson.marin.studybud.databinding.LayoutDashboardRemindersBinding
import com.mobdeve.s11.hartigango.juson.marin.studybud.helpers.Utility
import com.mobdeve.s11.hartigango.juson.marin.studybud.models.ReminderModel


class ReminderAdapter(options: FirestoreRecyclerOptions<ReminderModel>, context: Context) : FirestoreRecyclerAdapter<ReminderModel, ReminderAdapter.ReminderViewHolder>(options) {

    class ReminderViewHolder(val binding: LayoutDashboardRemindersBinding): RecyclerView.ViewHolder(binding.root) {
        fun bindData(reminder: ReminderModel){
            binding.reminderTitle.text = reminder.title
            binding.reminderTime.text = Utility.timestampToString(reminder.dateTime)
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReminderViewHolder {
        val itemViewBinding: LayoutDashboardRemindersBinding = LayoutDashboardRemindersBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ReminderViewHolder(itemViewBinding)
    }

    override fun onBindViewHolder(holder: ReminderViewHolder, position: Int, reminder: ReminderModel) {
        holder.bindData(reminder)
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, ReminderDetailsActivity::class.java)
            holder.itemView.context.startActivity(intent)
        }
    }

}
