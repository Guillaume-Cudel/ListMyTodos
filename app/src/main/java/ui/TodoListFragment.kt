package ui

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.guillaume.listmytodos.R
import com.guillaume.listmytodos.databinding.FragmentTodoListBinding
import model.Todo
import ui.adapter.TodoListAdapter
import viewmodel.TodoListViewModel


class TodoListFragment : Fragment(), Communicator {

    private lateinit var binding: FragmentTodoListBinding
    private lateinit var adapter: TodoListAdapter
    private val todoListVM: TodoListViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTodoListBinding.inflate(layoutInflater)

        todoListVM.getTodoList().observe(requireActivity(), Observer { todoList ->
            configureRecyclerView()
            setAdapter(todoList)
        })

        binding.fabAddTodo.setOnClickListener {
            openAddTodoDialog()
        }


        return binding.root
    }

    private fun configureRecyclerView(){
        val recyclerView = binding.todoListRecyclerview
        adapter = TodoListAdapter(this@TodoListFragment)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.addItemDecoration(
            DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL)
        )

    }

    private fun setAdapter(todos: List<Todo>){
        val sortListWithTime = todos.sortedBy { it.time }
        val reversedSortedList = sortListWithTime.reversed()
        val sortListWithBooleanAndTime = reversedSortedList.sortedBy { it.state }
        adapter.todoList = sortListWithBooleanAndTime
    }


    private fun openAddTodoDialog(){
        val builder = AlertDialog.Builder(requireActivity()).create()
        val dialogView = layoutInflater.inflate(R.layout.add_todo_dialog, null)
        builder.setView(dialogView)
        val title = dialogView.findViewById<EditText>(R.id.add_dialog_title_edit)
        val description = dialogView.findViewById<EditText>(R.id.add_dialog_description_edit)
        val urgentColor = dialogView.findViewById<ImageButton>(R.id.add_dialog_urgent_color)
        val saveButton = dialogView.findViewById<Button>(R.id.add_dialog_save_button)

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
            if(titleResponse != ""){
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

    override fun updateList(todos: List<Todo>) {
        adapter.notifyDataSetChanged()
        setAdapter(todos)
    }

    override fun passData(todo: Todo) {
        openDetailTodoDialog(todo)
    }

    private fun openDetailTodoDialog(todo: Todo){
        val detailBuilder = AlertDialog.Builder(requireActivity()).create()
        val view = layoutInflater.inflate(R.layout.detail_todo_dialog, null)
        detailBuilder.setView(view)
        val titleText = view.findViewById<TextView>(R.id.detail_title)
        val descriptionText = view.findViewById<TextView>(R.id.detail_description)
        val urgentText = view.findViewById<TextView>(R.id.detail_urgent_text)
        val urgentColor = view.findViewById<ImageView>(R.id.detail_urgent_color)
        val urgentWarning1 = view.findViewById<ImageView>(R.id.detail_urgent_warning1)
        val urgentWarning2 = view.findViewById<ImageView>(R.id.detail_urgent_warning2)
        val button = view.findViewById<Button>(R.id.detail_button)

        urgentUpdate(urgentText, urgentWarning1, urgentWarning2, todo.urgent)
        displayTodo(titleText, descriptionText, urgentColor, todo)
        button.setOnClickListener {
            detailBuilder.dismiss()
        }

        detailBuilder.setCanceledOnTouchOutside(true)
        detailBuilder.show()
    }

    private fun displayTodo(title: TextView, description: TextView, color: ImageView, todo: Todo){
        title.text = todo.title
        if(todo.description != "") description.text = todo.description
        else description.text = getString(R.string.no_description)

        adapter.changeUrgentColor(todo.urgent, color)
    }

    private fun urgentUpdate(urgentText: TextView, warning1: ImageView, warning2: ImageView, urgentNumber: Int){
        warning1.isVisible = false
        warning2.isVisible = false
        when(urgentNumber){
            1 -> urgentText.text = getString(R.string.no_urgent)
            2 -> urgentText.text = getString(R.string.medium_urgent)
            3 -> { urgentText.text = getString(R.string.very_urgent)
                warning1.isVisible = true
                warning2.isVisible = true
            }
        }
    }
}