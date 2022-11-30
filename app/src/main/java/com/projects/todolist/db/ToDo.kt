package com.projects.todolist.db

import androidx.room.Entity
import androidx.room.PrimaryKey



@Entity
data class ToDo (var todo : String,
                 var description:String,
                 var category: String,
                 var date:Long,
                 var time:Long,
                 var isFinished : Int = 0

){
    @PrimaryKey(autoGenerate = true)
    var id : Int = 0

}