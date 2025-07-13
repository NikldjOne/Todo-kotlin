package com.example.todo_kotlin

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todo_kotlin.adapter.TaskAdapter
import com.example.todo_kotlin.databinding.ActivityMainBinding
import com.example.todo_kotlin.model.Task
import com.example.todo_kotlin.model.TaskFilter

class MainActivity : ComponentActivity() {
    private lateinit var binding: ActivityMainBinding;
    private val mutableList: MutableList<Task> = mutableListOf()
    private lateinit var adapter: TaskAdapter
    private var currentFilter: TaskFilter = TaskFilter.ALL

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupInsets()
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        updateAdapter()
        setupAddTaskButton()
        setupFilterRadioGroup()
    }

    private fun updateAdapter() {
        adapter = TaskAdapter(
            getFilteredTasks(),
            ::onTaskCheckedChange,
            ::onTaskDelete
        )
        binding.recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    private fun setupAddTaskButton() {
        binding.materialAddTask.setOnClickListener {
            val editText = binding.textInputEditText.text.toString()
            if (editText.isNotEmpty()) {
                hideKeyBoard()
                mutableList.add(Task(editText, false))
                adapter.notifyItemInserted(mutableList.size - 1)
                binding.textInputEditText.text?.clear()
                binding.recyclerView.scrollToPosition(mutableList.size - 1)
                updatePercentProgressBar()
            }
        }
    }

    private fun updatePercentProgressBar() {
        val total = mutableList.size
        val done = mutableList.count { it.isChecked }
        val percent = if (total > 0) (done * 100) / total else 0
        binding.progressBar.progress = percent
    }

    private fun getFilteredTasks(): List<Task> = when (currentFilter) {
        TaskFilter.ALL -> mutableList
        TaskFilter.DONE -> mutableList.filter { it.isChecked }
        TaskFilter.UNFULFILLED -> mutableList.filter { !it.isChecked }
    }

    private fun setupFilterRadioGroup() {
        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            currentFilter = when (checkedId) {
                R.id.radio_all -> TaskFilter.ALL
                R.id.radio_done -> TaskFilter.DONE
                R.id.radio_unfulfiled -> TaskFilter.UNFULFILLED
                else -> TaskFilter.ALL
            }
            updateAdapter()
        }
    }


    private fun onTaskCheckedChange(position: Int, isChecked: Boolean) {
        val task = mutableList.removeAt(position)
        task.isChecked = isChecked
        if (isChecked) {
            mutableList.add(0, task)
        } else {
            mutableList.add(task)
        }
        adapter.notifyDataSetChanged()
        updatePercentProgressBar()
    }

    private fun onTaskDelete(position: Int) {
        mutableList.removeAt(position)
        adapter.notifyDataSetChanged()
        updatePercentProgressBar()
    }

    private fun hideKeyBoard() {
        val view = this.currentFocus ?: View(this)
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun setupInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val navBarHeight = insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom
            view.updatePadding(bottom = navBarHeight)
            insets
        }
    }
}

