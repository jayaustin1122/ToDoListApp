package com.projects.todolist

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.projects.todolist.databinding.ActivityMainBinding
import com.projects.todolist.databinding.AddDialogBinding
import com.projects.todolist.databinding.UpdateDialogBinding
import com.projects.todolist.db.ToDo
import com.projects.todolist.db.WorksDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity(){
    lateinit var binding: ActivityMainBinding
    lateinit var worksDB: WorksDatabase
    lateinit var adapter: TodoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        worksDB = WorksDatabase.invoke(this)
        view()
        swipeToDelete()

        binding.topAppBar.setNavigationOnClickListener(){

        }
        binding.floatingActionButton.setOnClickListener(){
            addDialog()
        }


        binding.topAppBar.setOnMenuItemClickListener(){menuItem->
            when(menuItem.itemId){
                R.id.aboutApp->{
                    addDialog()
                    true
                }
                else -> {false}
            }
        }
        binding.btnDeleteAll.setOnClickListener(){
            GlobalScope.launch(Dispatchers.IO) {
                worksDB.getToDo().deleteAll()
                view()
            }
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
                    adapter.notifyItemRemoved(position)
                    Snackbar.make(
                        binding.main,
                        "Item Deleted",
                        Snackbar.LENGTH_SHORT
                    ).apply {
                        setAction("Undo"){
                            adapter.todoModel.add(item)
                            adapter.notifyDataSetChanged()
                        }
                        show()
                    }
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
    private fun addDialog() {
        val dialog = Dialog(this)
        val binding: AddDialogBinding = AddDialogBinding.inflate(layoutInflater)
        dialog.setContentView(binding.root)
        dialog.show()
        binding.btnAdd2.setOnClickListener() {
            if(binding.etNewName.length() >=1 ) {
                var name: String = binding.etNewName.text.toString()
                val todo = ToDo(name)
                save(todo)
                adapter.todoModel.add(todo)
                adapter.notifyDataSetChanged()

                Toast.makeText(applicationContext, "Saved!", Toast.LENGTH_LONG).show()
                dialog.dismiss()
            }else{
                Toast.makeText(applicationContext, "You cannot add a empty activity!", Toast.LENGTH_LONG).show()
            }
        }
        binding.btnReset2.setOnClickListener() {
            binding.etNewName.text?.clear()
        }
    }
    private fun swipeToDelete(){
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT,

        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val item = adapter.todoModel[position]

                adapter.todoModel.removeAt(position)
                adapter.notifyItemRemoved(position)
                Snackbar.make(binding.main, "Item Deleted", Snackbar.LENGTH_SHORT)
                    .apply {
                        setAction("Undo"){
                        adapter.todoModel.add(item)
                        adapter.notifyDataSetChanged()
                    }
                    show()
                }
            }

        }).attachToRecyclerView(binding.myRecycler)
    }
}