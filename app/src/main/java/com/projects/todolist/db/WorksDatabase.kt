package com.projects.todolist.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [ToDo::class],
    version = 1
)
abstract class WorksDatabase:RoomDatabase() {
    abstract fun getToDo():ToDoDao

    companion object {
        // to access all
        @Volatile
        private var instance: WorksDatabase? = null
        private val LOCK = Any()

        //cheking the db if exist
        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        //to create db if db is not created
        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            WorksDatabase::class.java,
            "works"
        ).build()
    }
}