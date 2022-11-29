package com.projects.todolist.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import org.intellij.lang.annotations.Flow

@Dao
interface ToDoDao {
    @Insert
    fun addToDo(toDo: ToDo)

    @Query("SELECT * FROM ToDo")
    fun getAllToDos():MutableList<ToDo>

    @Query("UPDATE ToDo SET todo = :name WHERE id = :id")
    fun updateToDo(name:String,id:Int)

    @Query("DELETE FROM ToDo WHERE id = :id")
    fun deleteToDo(id:Int)

    @Query("DELETE  FROM TODO")
    fun deleteAll()
    @Query("SELECT * FROM TODO WHERE TODO LIKE :searchQuery")
    fun searchDataBase(searchQuery:String):MutableList<ToDo>
}
