package com.dicoding.submissionawalaplikasigithubuser.bookmark

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: EntityUser)

    @Query("SELECT * FROM user")
    fun loadAll(): LiveData<List<EntityUser>>

    @Query("SELECT * FROM user WHERE id LIKE :id LIMIT 1")
    fun findById(id: Int): EntityUser

    @Delete
    fun delete(user: EntityUser)
}