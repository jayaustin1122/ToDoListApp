package com.projects.todolist.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import org.intellij.lang.annotations.Flow

@Dao
interface ToDoDao {
    @Insert
    fun addToDo(toDo: ToDo)

    @Query("SELECT * FROM todolist")
    fun getAllToDos():MutableList<ToDo>

    @Query("UPDATE todolist SET todo = :name, time =:time,date = :date where id like :id")
    fun updateToDo(name:String,
                   date:String,
                   time: String,
                   id: Long
    )

    @Query("DELETE FROM todolist WHERE id = :id")
    fun deleteToDo(id:Long)

    @Query("DELETE  FROM todolist")
    fun deleteAll()

    @Query("SELECT * from todolist where id Like :id")
    fun get(id : Long): ToDo

}
