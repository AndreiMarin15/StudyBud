package com.mobdeve.s11.hartigango.juson.marin.studybud.models

import com.google.firebase.Timestamp

class TaskModel(var task: String, var todoDate: Timestamp, var deadline: Timestamp, var category: String, var status: Boolean, var notes: String) {
    constructor(): this("N/A", Timestamp.now(), Timestamp.now(), "N/A", false, "N/A")
}
