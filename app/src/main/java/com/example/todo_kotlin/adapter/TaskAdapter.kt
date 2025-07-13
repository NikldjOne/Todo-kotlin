package com.example.todo_kotlin.adapter

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView

import com.example.todo_kotlin.databinding.ItemTodoBinding
import com.example.todo_kotlin.model.Task


class TaskAdapter(
    private val dataSet: List<Task>,
    private val onCheckedChange: (Int, Boolean) -> Unit,
    private val onDelete: (Int) -> Unit
) : RecyclerView.Adapter<TaskAdapter.ViewHolder>() {

    class ViewHolder(private val binding: ItemTodoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(task: Task, onCheckedChange: (Boolean) -> Unit, onDelete: () -> Unit) {
            binding.textView.text = task.text
            binding.checkBox.setOnCheckedChangeListener(null)
            binding.checkBox.isChecked = task.isChecked

            binding.textView.paintFlags = if (task.isChecked)
                binding.textView.paintFlags or android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
            else binding.textView.paintFlags and android.graphics.Paint.STRIKE_THRU_TEXT_FLAG.inv()
            binding.checkBox.setOnCheckedChangeListener { _, isChecked ->
                onCheckedChange(isChecked)
            }
            binding.materialButton.setOnClickListener {
                onDelete()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTodoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(
            dataSet[position],
            { isChecked ->
                onCheckedChange(position, isChecked)
            },
            { onDelete(position) })
    }

    override fun getItemCount() = dataSet.size

}