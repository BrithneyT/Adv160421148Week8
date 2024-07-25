package com.example.todoapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.todoapp.model.Todo
import com.example.todoapp.util.buildDb
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ListTodoViewModel(application: Application) : AndroidViewModel(application) {
    val todoLD = MutableLiveData<List<Todo>>()
    val todoLoadErrorLD = MutableLiveData<Boolean>()
    val loadingLD = MutableLiveData<Boolean>()

    fun refresh() {
        loadingLD.value = true
        todoLoadErrorLD.value = false
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val db = buildDb(getApplication())
                val todos = db.todoDao().selectAllActiveTodo()
                withContext(Dispatchers.Main) {
                    todoLD.value = todos
                    loadingLD.value = false
                }
            }
        }
    }

    fun clearTask(todo: Todo) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val db = buildDb(getApplication())
                db.todoDao().markTodoAsDone(todo.uuid)
                val updatedTodos = db.todoDao().selectAllActiveTodo()
                withContext(Dispatchers.Main) {
                    todoLD.value = updatedTodos
                }
            }
        }
    }
}