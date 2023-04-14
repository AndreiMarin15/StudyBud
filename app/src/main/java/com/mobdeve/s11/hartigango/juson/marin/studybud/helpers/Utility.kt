package com.mobdeve.s11.hartigango.juson.marin.studybud.helpers

import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.Timestamp
import com.google.firebase.firestore.*
import com.mobdeve.s11.hartigango.juson.marin.studybud.models.*
import java.text.SimpleDateFormat
import java.util.*

class Utility {
    companion object {


        fun getCollectionReferenceForUsers(): CollectionReference { // returns the collection of all users in the database
            return  FirebaseFirestore.getInstance().collection("userInfo")
        }

        fun getCollectionReferenceForLists(docID: String): CollectionReference { // returns the collection of the lists of the logged in user
            return FirebaseFirestore.getInstance().collection("userInfo").document(docID).collection("lists")
        }



        fun getCollectionReferenceForReminders(docID: String): CollectionReference { // returns the collection for all reminders of the logged in user

            return  FirebaseFirestore.getInstance().collection("userInfo").document(docID).collection("lists")
                .document(" Reminders ").collection("remindersList")
        }


        fun getCollectionReferenceForTasks(category: String, docID: String): CollectionReference { // returns the list of tasks inside a category
            return  FirebaseFirestore.getInstance().collection("userInfo").document(docID).collection("lists")
                .document(category).collection("tasksList")
        }

        fun getCollectionReferenceForAllTasks(docID: String): CollectionReference { // returns the list of ALL tasks owned by the logged in user
            return  FirebaseFirestore.getInstance().collection("userInfo").document(docID).collection("allTasks")

        }

        fun updateTask(category: String, docID: String, document: String, data: Map<String, Boolean>?, data2: Map<String, Any>?): Task<Void> {
            // updates a task from the database
            val docRef: DocumentReference = getCollectionReferenceForTasks(category, docID).document(document)
            val docRef2: DocumentReference = getCollectionReferenceForAllTasks(docID).document(document)

            if(data != null) {
                val updateTask1 = docRef.update(data)
                val updateTask2 = docRef2.update(data)
                return Tasks.whenAll(updateTask1, updateTask2)
            } else {
                val updateTask1 = docRef.update(data2!!)
                val updateTask2 = docRef2.update(data2)
                return Tasks.whenAll(updateTask1, updateTask2)
            }

        }

        fun deleteTask(category: String, docID: String, document: String,){
            // deletes a task from the database
            val docRef: DocumentReference = getCollectionReferenceForTasks(category, docID).document(document)
            val docRef2: DocumentReference = getCollectionReferenceForAllTasks(docID).document(document)

            docRef.delete()
            docRef2.delete()
        }

        fun setTask (category: String, task: TaskModel, docID: String){
            // creates a task in the database
            val docRef: DocumentReference = getCollectionReferenceForTasks(category, docID).document()
            val docRef2: DocumentReference = getCollectionReferenceForAllTasks(docID).document(docRef.id)
            docRef.set(task)
            docRef2.set(task)
        }

        fun setReminder (reminder: ReminderModel, docID: String) {
            // creates a reminder in the database
            val docRef: DocumentReference = getCollectionReferenceForReminders(docID).document()
            docRef.set(reminder)
        }


        fun setUserInfo (user: UserInfoModel){
            // adds the information for new users inside the database
            val docRef: DocumentReference = getCollectionReferenceForUsers().document()
            docRef.set(user)

        }

        fun setList (list: ListModel, docID: String){
            // creates a list in the database
            val docRef: DocumentReference = getCollectionReferenceForLists(docID).document(list.name)
            docRef.set(list)
        }

        fun timestampToString(timestamp: Timestamp): String{
            val dateFormat = SimpleDateFormat("MMMM d, yyyy 'at' h:mm a", Locale.US)
            return dateFormat.format(timestamp.toDate())
        }

    }


}