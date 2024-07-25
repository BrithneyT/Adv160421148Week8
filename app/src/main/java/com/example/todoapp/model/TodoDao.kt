package com.example.todoapp.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TodoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg todo: Todo)

    @Query("SELECT * FROM todo WHERE is_done = 0 ORDER BY priority DESC")// Menggunakan INTEGER bukan BOOLEAN karena SQLite tidak memiliki tipe data BOOLEAN bawaan jadi diganti dengan integer dengan 1= true dan 0= false.
    fun selectAllActiveTodo(): List<Todo>

    @Query("SELECT * FROM todo WHERE uuid= :id")
    fun selectTodo(id:Int): Todo

    @Delete
    fun deleteTodo(todo: Todo)

    @Query("UPDATE todo SET title=:title, notes=:notes, priority=:priority, is_done=:isDone WHERE uuid = :id")
    fun update(title:String, notes:String, priority:Int, isDone:Int, id:Int)

    @Query("UPDATE todo SET is_done = 1 WHERE uuid = :id")
    fun markTodoAsDone(id: Int)
}