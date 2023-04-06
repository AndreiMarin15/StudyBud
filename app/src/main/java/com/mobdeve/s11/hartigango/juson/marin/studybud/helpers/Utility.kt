package com.mobdeve.s11.hartigango.juson.marin.studybud.helpers

import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.Timestamp
import com.google.firebase.firestore.*
import com.mobdeve.s11.hartigango.juson.marin.studybud.models.*
import java.text.SimpleDateFormat
import java.util.*

class Utility {
    companion object {


        fun getCollectionReferenceForUsers(): CollectionReference {
            return  FirebaseFirestore.getInstance().collection("userInfo")
        }

        fun getCollectionReferenceForLists(docID: String): CollectionReference {
            return FirebaseFirestore.getInstance().collection("userInfo").document(docID).collection("lists")
        }



        fun getCollectionReferenceForReminders(docID: String): CollectionReference {

            return  FirebaseFirestore.getInstance().collection("userInfo").document(docID).collection("lists")
                .document(" Reminders ").collection("remindersList")
        }


        fun getCollectionReferenceForTasks(category: String, docID: String): CollectionReference {
            return  FirebaseFirestore.getInstance().collection("userInfo").document(docID).collection("lists")
                .document(category).collection("tasksList")
        }

        fun getCollectionReferenceForAllTasks(docID: String): CollectionReference {
            return  FirebaseFirestore.getInstance().collection("userInfo").document(docID).collection("allTasks")

        }

        fun updateTask(category: String, docID: String, document: String, data: Map<String, Boolean>){
            val docRef: DocumentReference = getCollectionReferenceForTasks(category, docID).document(document)
            val docRef2: DocumentReference = getCollectionReferenceForAllTasks(docID).document(document)

            docRef.update(data)
            docRef2.update(data)
        }

        fun setTask (category: String, task: TaskModel, docID: String){
            val docRef: DocumentReference = getCollectionReferenceForTasks(category, docID).document()
            val docRef2: DocumentReference = getCollectionReferenceForAllTasks(docID).document(docRef.id)
            docRef.set(task)
            docRef2.set(task)
        }

        fun setReminder (reminder: ReminderModel, docID: String) {
            val docRef: DocumentReference = getCollectionReferenceForReminders(docID).document()
            docRef.set(reminder)
        }


        fun setUserInfo (user: UserInfoModel){
            val docRef: DocumentReference = getCollectionReferenceForUsers().document()
            docRef.set(user)

        }

        fun setList (list: ListModel, docID: String){
            val docRef: DocumentReference = getCollectionReferenceForLists(docID).document(list.name)
            docRef.set(list)
        }



      //  fun setTaskList (taskList: TaskListModel, docID: String) {
      //      val docRef: DocumentReference = getCollectionReferenceForTaskList(docID).document()
      //      docRef.set(taskList)
      //  }

        fun timestampToString(timestamp: Timestamp): String{
            val dateFormat = SimpleDateFormat("MMMM d, yyyy 'at' h:mm a", Locale.US)
            return dateFormat.format(timestamp.toDate())
        }




    }


}