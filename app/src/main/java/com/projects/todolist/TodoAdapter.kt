package com.projects.todolist

import android.annotation.SuppressLint
import android.view.*
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.projects.todolist.databinding.RowItemBinding
import com.projects.todolist.db.ToDo


class TodoAdapter(var todoModel : MutableList<ToDo>):RecyclerView.Adapter<TodoAdapter.ToDoViewHolder>() {

    var onItemDelete : ((ToDo, Int) -> Unit) ? = null
    var onUpdate : ((ToDo, Int) -> Unit) ? = null
    inner class ToDoViewHolder(var binding: RowItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RowItemBinding.inflate(layoutInflater, parent, false)
        return ToDoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ToDoViewHolder,position: Int) {
        holder.binding.apply {
            checkBox.text = todoModel[position].todo
            btnDelete.setOnClickListener(){
                onItemDelete?.invoke(todoModel[position],position)
            }
            btnEdit.setOnClickListener() {
                onUpdate?.invoke(todoModel[position], position)
            }
        }
    }

    override fun getItemCount(): Int {
        return todoModel.size
    }

}