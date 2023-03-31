package com.mobdeve.s11.hartigango.juson.marin.studybud

import com.mobdeve.s11.hartigango.juson.marin.studybud.models.TaskModel

class TaskHelper {
    companion object {
        fun initializeData(): ArrayList<TaskModel> {
            val tasks = ArrayList<TaskModel>()

            tasks.add(
                TaskModel(
                    "CCAPDEV User Interface"
                )
            )

            tasks.add(
                TaskModel(
                    "Project Review"
                )
            )

            tasks.add(
                TaskModel(
                    "Weekly Update"
                )
            )

            tasks.add(
                TaskModel(
                    "MOBDEVE MCO1"
                )
            )

            tasks.add(
                TaskModel(
                    "LCASEAN Midterm"
                )
            )

            return tasks
        }
    }
}