package com.mobdeve.s11.hartigango.juson.marin.studybud

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

class Utility {
    companion object {
        fun getCollectionReferenceForUsers(): CollectionReference {
            val currentUser: FirebaseUser = FirebaseAuth.getInstance().currentUser!!
            return  FirebaseFirestore.getInstance().collection("userInfo")
        }
    }


}