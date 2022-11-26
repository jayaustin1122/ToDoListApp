package com.projects.todolist.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ToDo (var todo : String){
    @PrimaryKey(autoGenerate = true)
    var id : Int = 0
}