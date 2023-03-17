package com.mobdeve.s11.hartigango.juson.marin.studybud

class ReminderHelper {
    companion object {
        fun initializeData(): ArrayList<ReminderModel> {

            val reminder = ArrayList<ReminderModel>()

            reminder.add(
                ReminderModel(
                "CCINFOM Concept Exam",
                "Today 12:45 PM"
            )
            )

            reminder.add(ReminderModel(
                "CCAPDEV MP Demo",
                "March 20, 7:30 AM"
            ))

            reminder.add(ReminderModel(
                "LSCS General Assembly",
                "March 20, 2:30PM"
            ))

            reminder.add(ReminderModel(
                "CSG General Assembly",
                "March 21, 2:30PM"
            ))

            reminder.add(ReminderModel(
                "MOBDEVE Phase 2",
                "March 21, 2:30PM"
            ))

            return reminder
        }
    }
}