package com.mobdeve.s11.hartigango.juson.marin.studybud.models

import com.google.firebase.Timestamp

class ReminderModel(var title: String, var dateTime: Timestamp, var notes: String){
    constructor(): this("N/A", Timestamp.now(),  "N/A")
}