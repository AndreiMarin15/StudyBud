package com.mobdeve.s11.hartigango.juson.marin.studybud

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mobdeve.s11.hartigango.juson.marin.studybud.databinding.LayoutReminderBinding
import com.mobdeve.s11.hartigango.juson.marin.studybud.helpers.Utility
import com.mobdeve.s11.hartigango.juson.marin.studybud.models.ListModel
import io.grpc.okhttp.internal.Util

class ListsAdapter(options: FirestoreRecyclerOptions<ListModel>, context: Context, docId: String) : FirestoreRecyclerAdapter<ListModel, ListsAdapter.ListsViewHolder>(options) {
    val id = docId
    class ListsViewHolder(val binding: LayoutReminderBinding, context: Context): RecyclerView.ViewHolder(binding.root) {
        private val sp = context.applicationContext.getSharedPreferences("UserInfo", Context.MODE_PRIVATE)
        private val docId = sp.getString("DOCID", "N/A")

        fun bindData(list: ListModel){
            if(list.name == " Reminders "){
                binding.btnDel.visibility = View.INVISIBLE
                binding.btnDel.isEnabled = false
                val docRef =  Utility.getCollectionReferenceForLists(docId!!).document(list.name).collection("remindersList")
                var count = 0
                docRef.get().addOnSuccessListener {
                    count = it.size()

                    binding.listName.text = list.name
                    if(count != 1){ binding.itemCount.text = "$count Items" } else { binding.itemCount.text = "$count Item"}
                }.addOnFailureListener {
                    binding.listName.text = list.name
                    if(count != 1){ binding.itemCount.text = "$count Items" } else { binding.itemCount.text = "$count Item"}
                }
            } else {
                val docRef =  Utility.getCollectionReferenceForLists(docId!!).document(list.name).collection("tasksList")
                var count = 0

                docRef.get().addOnSuccessListener {
                    count = it.size()

                    binding.listName.text = list.name
                    if(count != 1){ binding.itemCount.text = "$count Items" } else { binding.itemCount.text = "$count Item"}
                }.addOnFailureListener {
                    binding.listName.text = list.name
                    if(count != 1){ binding.itemCount.text = "$count Items" } else { binding.itemCount.text = "$count Item"}
                }
            }


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListsViewHolder {
        val itemViewBinding: LayoutReminderBinding = LayoutReminderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListsViewHolder(itemViewBinding, parent.context)



    }

    override fun onBindViewHolder(holder: ListsViewHolder, position: Int, list: ListModel) {
        holder.bindData(list)


        if(holder.binding.listName.text == " Reminders "){
            holder.binding.btnDel.visibility = View.INVISIBLE
            holder.binding.btnDel.isEnabled = false
        } else {
            holder.binding.btnDel.setOnClickListener {
                Utility.getCollectionReferenceForTasks(list.name, id)
                    .get()
                    .addOnSuccessListener { documents ->
                        var doc: String
                        for(document in documents){
                            doc = document.id
                            Utility.deleteTask(list.name, id, doc)
                        }

                        Utility.getCollectionReferenceForLists(id)
                            .whereEqualTo("name", list.name)
                            .whereEqualTo("owner", list.owner)
                            .limit(1)
                            .get()
                            .addOnSuccessListener {documents2 ->
                                if(!documents2.isEmpty){
                                    val docu = documents2.first()

                                    docu.reference.delete()
                                    Toast.makeText(holder.itemView.context, "List Deleted", Toast.LENGTH_SHORT).show()
                                }
                            }
                    }


                }
            }


        holder.itemView.setOnClickListener {
            if(holder.binding.listName.text == " Reminders "){
                val intent = Intent(holder.itemView.context, RemindersActivity::class.java)

                holder.itemView.context.startActivity(intent)
            } else {
                val intent = Intent(holder.itemView.context, TasksActivity::class.java)
                intent.putExtra("category", list.name)
                holder.itemView.context.startActivity(intent)
            }
        }
    }
}