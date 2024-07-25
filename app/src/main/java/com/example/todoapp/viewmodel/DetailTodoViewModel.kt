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

class DetailTodoViewModel(application: Application) : AndroidViewModel(application) {
    val todoLD = MutableLiveData<Todo>()

    fun fetch(uuid: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val db = buildDb(getApplication())
                val todo = db.todoDao().selectTodo(uuid)
                withContext(Dispatchers.Main) {
                    todoLD.value = todo
                }
            }
        }
    }

    fun addTodo(list: List<Todo>) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val db = buildDb(getApplication())
                db.todoDao().insertAll(*list.toTypedArray())
            }
        }
    }

    fun update(title: String, notes: String, priority: Int, uuid: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val db = buildDb(getApplication())
                db.todoDao().update(title, notes, priority, 0, uuid)
            }
        }
    }
}