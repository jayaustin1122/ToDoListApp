package com.projects.todolist

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.projects.todolist.databinding.ActivityMainBinding
import com.projects.todolist.databinding.UpdateDialogBinding
import com.projects.todolist.db.ToDo
import com.projects.todolist.db.WorksDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var worksDB: WorksDatabase
    lateinit var adapter: TodoAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        worksDB = WorksDatabase.invoke(this)
        view()
        binding.btnAdd.setOnClickListener() {
            var name: String = binding.etName.text.toString()
            val todo = ToDo(name)
            save(todo)
            adapter.todoModel.add(todo)
            adapter.notifyDataSetChanged()

            Toast.makeText(applicationContext, "Saved!", Toast.LENGTH_LONG).show()
        }
    }

    private fun delete(toDo: ToDo) {
        GlobalScope.launch(Dispatchers.IO) {
            worksDB.getToDo().deleteToDo(toDo.id)
            view()
        }
    }

    private fun save(toDo: ToDo) {
        GlobalScope.launch(Dispatchers.IO) {
            worksDB.getToDo().addToDo(toDo)
            view()
        }
    }

    private fun view() {
        lateinit var todos: MutableList<ToDo>
        GlobalScope.launch(Dispatchers.IO) {
            todos = worksDB.getToDo().getAllToDos()

            withContext(Dispatchers.Main) {
                adapter = TodoAdapter(todos)
                binding.myRecycler.adapter = adapter
                binding.myRecycler.layoutManager = LinearLayoutManager(applicationContext)

                adapter.onItemDelete = { item: ToDo, position: Int ->

                    delete(item)
                    adapter.todoModel.removeAt(position)
                    adapter.notifyDataSetChanged()
                }
                adapter.onUpdate = { item: ToDo, position: Int ->

                    showUpdateDialog(item.id)
                    adapter.notifyDataSetChanged()
                }

            }
        }

    }

    private fun showUpdateDialog(id: Int) {
        val dialog = Dialog(this)
        val binding: UpdateDialogBinding = UpdateDialogBinding.inflate(layoutInflater)
        dialog.setContentView(binding.root)
        dialog.show()

        binding.btnOK.setOnClickListener() {
            var newName: String = binding.etNewName.text.toString()
            GlobalScope.launch(Dispatchers.IO) {
                worksDB.getToDo().updateToDo(newName, id)
                view()
            }
            dialog.dismiss()
        }

    }
}