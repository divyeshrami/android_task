package com.example.clientdemo.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [UserTable::class], version = 1, exportSchema = false)
abstract class UserDatabase : RoomDatabase() {

    abstract fun userDoa(): UserDAO

    companion object {


        var INSTANCE: UserDatabase? = null

        open fun getDatabase(context: Context): UserDatabase? {
            if (INSTANCE == null) {
                synchronized(UserDatabase::class) {
                        INSTANCE = Room.databaseBuilder<UserDatabase>(
                            context.applicationContext, UserDatabase::class.java, "USER_DATABASE"
                        )
                            .fallbackToDestructiveMigration()
                            .build()
                }
            }
            return INSTANCE
        }

    }

    fun destroyDataBase(){
        INSTANCE = null
    }


}