package ui

import model.Todo

interface Communicator {

    fun updateList(todos: List<Todo>)

    fun passData(todo: Todo)
}