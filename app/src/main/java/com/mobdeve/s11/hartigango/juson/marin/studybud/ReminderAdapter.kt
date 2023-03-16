package com.mobdeve.s11.hartigango.juson.marin.studybud

import android.content.Intent
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import android.view.LayoutInflater
import androidx.activity.result.ActivityResultLauncher
import com.mobdeve.s11.hartigango.juson.marin.studybud.databinding.ReminderRecyclerBinding


class ReminderAdapter(private val reminderData: ArrayList<ReminderModel>, private val viewNoteLauncher: ActivityResultLauncher<Intent>) : Adapter<ReminderViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReminderViewHolder {
        val itemViewBinding: ReminderRecyclerBinding = ReminderRecyclerBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ReminderViewHolder(itemViewBinding)
    }

    override fun getItemCount(): Int {
        return reminderData.size
    }

    override fun onBindViewHolder(holder: ReminderViewHolder, position: Int) {
        holder.bindData(reminderData[position])
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, ReminderDetailsActivity::class.java)
            viewNoteLauncher.launch(intent)
        }
    }

}
