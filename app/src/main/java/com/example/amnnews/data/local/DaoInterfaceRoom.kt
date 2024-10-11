package com.example.amnnews.data.local
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Update
import androidx.room.Delete
import androidx.room.Query
import com.example.amnnews.data.remote.model.New

@Dao
interface DaoInterfaceRoom {

    @Insert
    suspend fun insert(article: New)

    @Update
    suspend fun update(article: New)

    @Delete
    suspend fun delete(article: New)

    @Query("SELECT * FROM news WHERE id = :id")
    suspend fun getById(id: Int): New?

    @Query("SELECT * FROM news")
    suspend fun getAll(): List<New>
}
