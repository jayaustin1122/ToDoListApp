package com.projects.todolist.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalTime

@Entity
data class ToDo (var todo : String){
    @PrimaryKey(autoGenerate = true)
    var id : Int = 0
}