package ui

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.guillaume.listmytodos.R
import com.guillaume.listmytodos.databinding.FragmentTodoListBinding
import model.Todo
import ui.adapter.TodoListAdapter
import viewmodel.TodoListViewModel


class TodoListFragment : Fragment() {

    private lateinit var binding: FragmentTodoListBinding
    private lateinit var adapter: TodoListAdapter
    private val todoListVM: TodoListViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTodoListBinding.inflate(layoutInflater)

        todoListVM.getTodoList().observe(requireActivity(), Observer {
            val list = it
            configureRecyclerView(list)
        })

        binding.fabAddTodo.setOnClickListener {
            openAddTodoDialog()
        }


        return binding.root
    }

    private fun configureRecyclerView(todos: List<Todo>){
        val recyclerView = binding.todoListRecyclerview
        adapter = TodoListAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.addItemDecoration(
            DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL)
        )
        adapter.todoList = todos
    }

    private fun openAddTodoDialog(){
        val builder = AlertDialog.Builder(requireActivity()).create()
        val dialogView = layoutInflater.inflate(R.layout.add_todo_dialog, null)
        builder.setView(dialogView)
        val title = dialogView.findViewById<EditText>(R.id.add_dialog_title_edit)
        val description = dialogView.findViewById<EditText>(R.id.add_dialog_description_edit)
        val urgentColor = dialogView.findViewById<ImageButton>(R.id.add_dialog_urgent_color)
        val saveButton = dialogView.findViewById<Button>(R.id.add_dialog_save_button)
        builder.setView(dialogView)

        var color = 1

        urgentColor.setOnClickListener {
            color++
            if(color > 3){
                color = 1
            }
            setColor(urgentColor, color)
        }


        saveButton.setOnClickListener {
            val titleResponse = title.editableText.toString()
            val descriptionResponse = description.editableText.toString()
            if(titleResponse != "" && descriptionResponse != ""){
                val time = System.currentTimeMillis()
                val todo = Todo(titleResponse, descriptionResponse, false, color, time)
                todoListVM.addTodoInList(todo)
                builder.dismiss()
            } else {
                Toast.makeText(requireActivity(), getString(R.string.fill_fields), Toast.LENGTH_LONG).show()
            }
        }

        builder.setCanceledOnTouchOutside(true)
        builder.show()
    }

    private fun setColor(image: ImageButton, color: Int){
        when(color){
            1 -> image.setBackgroundResource(R.drawable.round_green)
            2 -> image.setBackgroundResource(R.drawable.round_orange)
            3 -> image.setBackgroundResource(R.drawable.round_red)
        }
    }

}