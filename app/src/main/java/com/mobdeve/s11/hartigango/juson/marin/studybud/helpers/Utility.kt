package com.mobdeve.s11.hartigango.juson.marin.studybud.helpers

import android.content.Context
import android.content.SharedPreferences
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
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
                .document("Reminders").collection("remindersList")
        }


        fun getCollectionReferenceForTaskList(docID: String): CollectionReference {
            return  FirebaseFirestore.getInstance().collection("userInfo").document(docID).collection("lists")
                .document("taskList").collection("tasks")
        }

        fun getCollectionReferenceForTasks(category: String, task: String, docID: String): CollectionReference {
            return  getCollectionReferenceForTaskList(docID).document(category).collection(task)
        }

        fun setTask (category: String, task: String, subTask: TaskModel, docID: String){
            val docRef: DocumentReference = getCollectionReferenceForTasks(category, task, docID).document()
            docRef.set(subTask)
        }


        fun setUserInfo (user: UserInfoModel){
            val docRef: DocumentReference = getCollectionReferenceForUsers().document()
            docRef.set(user)

        }

        fun setList (list: ListModel, docID: String){
            val docRef: DocumentReference = getCollectionReferenceForLists(docID).document(list.name)
            docRef.set(list)
        }

        fun setReminder (reminder: ReminderModel, docID: String) {
            val docRef: DocumentReference = getCollectionReferenceForReminders(docID).document()
            docRef.set(reminder)
        }

        fun setTaskList (taskList: TaskListModel, docID: String) {
            val docRef: DocumentReference = getCollectionReferenceForTaskList(docID).document()
            docRef.set(taskList)
        }

        fun timestampToString(timestamp: Timestamp): String{
            val dateFormat = SimpleDateFormat("MMMM d, yyyy 'at' h:mm a", Locale.US)
            return dateFormat.format(timestamp.toDate())
        }




    }


}