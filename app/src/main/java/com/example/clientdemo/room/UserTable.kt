package com.example.clientdemo.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "UserDetail")
data class UserTable(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "Id")
    val id: Int,
    @ColumnInfo(name = "UserName")
    var userName: String,
    @ColumnInfo(name = "Header")
    var headerToken: String
)