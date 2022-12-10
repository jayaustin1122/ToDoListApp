package com.projects.todolist.db

import androidx.room.Entity
import androidx.room.PrimaryKey



@Entity(tableName = "todolist")
data class ToDo (var todo : String,
                 val time : String,
                 val date : String

){
    @PrimaryKey(autoGenerate = true)
    var id : Long = 0

}