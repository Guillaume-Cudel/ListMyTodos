package com.guillaume.listmytodos

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import model.Todo
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.any
import viewmodel.TodoListViewModel

class UnitTests {

    private val todoListVM: TodoListViewModel = TodoListViewModel()

    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()




    @Test
    fun addTodoInList(){
        val todo = Todo("Faire la vaisselle", any(), false, 1, 1234)
        todoListVM.addTodoInList(todo)

        val todoList = todoListVM.getTodoList().value
        assertTrue(todoList!!.isNotEmpty())
        assertEquals(todoList[0], todo)
    }

}