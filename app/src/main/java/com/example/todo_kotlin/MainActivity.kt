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

class MainActivity : ComponentActivity() {
    private lateinit var binding: ActivityMainBinding;
    private val mutableList: MutableList<String> = mutableListOf()
    private lateinit var adapter: TaskAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val navBarHeight = insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom
            view.updatePadding(bottom = navBarHeight)
            insets
        }
        adapter = TaskAdapter(mutableList)
        binding.recyclerView.adapter = adapter
        binding.materialAddTask.setOnClickListener {
            val editText = binding.textInputEditText.text.toString()
            if (editText.isNotEmpty()) {
                mutableList.add(editText)
                binding.textInputEditText.text?.clear()
                adapter.notifyItemInserted(mutableList.size - 1)
                binding.recyclerView.scrollToPosition(mutableList.size - 1)

            }
        }

    }
    private fun hideKeyBoard() {
        val view = this.currentFocus ?: View(this)
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken,0)
    }
}

