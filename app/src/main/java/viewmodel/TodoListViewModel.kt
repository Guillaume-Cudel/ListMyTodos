package viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import model.Todo

class TodoListViewModel: ViewModel() {

    private val _todoList = MutableLiveData<List<Todo>>()
    private val todoList: LiveData<List<Todo>> = _todoList
    private val list: MutableList<Todo> = mutableListOf()


    fun getTodoList(): LiveData<List<Todo>>{
        return todoList
    }

    fun addTodoInList(todo: Todo){
        list.add(todo)
        _todoList.value = list
    }


}