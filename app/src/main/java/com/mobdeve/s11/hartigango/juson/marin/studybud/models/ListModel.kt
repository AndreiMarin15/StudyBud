package com.mobdeve.s11.hartigango.juson.marin.studybud.models

import com.google.firebase.Timestamp

class ListModel(var name: String, var owner: String, dateCreated: Timestamp) {
    constructor() : this("default", "N/A", Timestamp.now())
}