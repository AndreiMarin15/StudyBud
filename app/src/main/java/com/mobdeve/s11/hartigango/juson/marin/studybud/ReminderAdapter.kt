package com.mobdeve.s11.hartigango.juson.marin.studybud

import android.content.Context
import android.content.Intent
import android.view.ViewGroup
import android.view.LayoutInflater
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.mobdeve.s11.hartigango.juson.marin.studybud.databinding.LayoutDashboardRemindersBinding
import com.mobdeve.s11.hartigango.juson.marin.studybud.helpers.Utility
import com.mobdeve.s11.hartigango.juson.marin.studybud.models.ReminderModel


class ReminderAdapter(options: FirestoreRecyclerOptions<ReminderModel>, context: Context, docId: String) : FirestoreRecyclerAdapter<ReminderModel, ReminderAdapter.ReminderViewHolder>(options) {
    val id = docId
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
            intent.putExtra("title", reminder.title)
            intent.putExtra("dateTime", reminder.dateTime.toString())
            intent.putExtra("notes", reminder.notes)

            holder.itemView.context.startActivity(intent)
        }

        holder.binding.deleteBtn.setOnClickListener {
                Utility.getCollectionReferenceForReminders(id)
                    .whereEqualTo("title", reminder.title)
                    .limit(1)
                    .get().addOnSuccessListener {document ->

                        if(!document.isEmpty){
                            val doc = document.first()

                            doc.reference.delete()
                            Toast.makeText(holder.itemView.context, "Reminder deleted", Toast.LENGTH_SHORT).show()
                        }

                }

        }
    }

}
