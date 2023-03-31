package com.mobdeve.s11.hartigango.juson.marin.studybud

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

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
    }


}