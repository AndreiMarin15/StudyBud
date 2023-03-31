package com.mobdeve.s11.hartigango.juson.marin.studybud.firestorehelpers

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.mobdeve.s11.hartigango.juson.marin.studybud.models.TaskModel
import com.mobdeve.s11.hartigango.juson.marin.studybud.models.ReminderModel
import com.mobdeve.s11.hartigango.juson.marin.studybud.models.UserInfoModel

class Utility {
    companion object {
        fun getCollectionReferenceForUsers(): CollectionReference {
            return  FirebaseFirestore.getInstance().collection("userInfo")
        }
        fun getCollectionReferenceForReminders(): CollectionReference {
            return  FirebaseFirestore.getInstance().collection("reminders")
        }

        fun getCollectionReferenceForTasks(): CollectionReference {
            return  FirebaseFirestore.getInstance().collection("tasks")
        }

        fun setUserInfo (user: UserInfoModel){
            val docRef: DocumentReference = getCollectionReferenceForUsers().document()
            docRef.set(user)

        }

        fun setReminder (reminder: ReminderModel) {
            val docRef: DocumentReference = getCollectionReferenceForReminders().document()
            docRef.set(reminder)
        }

        fun setTask (task: TaskModel) {
            val docRef: DocumentReference = getCollectionReferenceForTasks().document()
            docRef.set(task)
        }




    }


}