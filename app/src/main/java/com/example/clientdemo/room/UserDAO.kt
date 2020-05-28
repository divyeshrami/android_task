package com.example.clientdemo.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDAO {

    @Insert
    fun insertDetails(data: UserTable)

    @Query("select * from UserDetail")
    fun getDetails(): LiveData<List<UserTable>>

    @Query("delete from UserDetail")
    fun deleteAllData()
}