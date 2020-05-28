package com.example.clientdemo.login

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import com.example.clientdemo.room.UserDAO
import com.example.clientdemo.room.UserDatabase
import com.example.clientdemo.room.UserTable


class LoginRepository(application: Application)  {

    var userDAO: UserDAO
    val userList: LiveData<List<UserTable>>

    init {
        val db = UserDatabase.getDatabase(application)
        userDAO = db?.userDoa()!!
        userList = userDAO.getDetails()
    }

    fun getAllData(): LiveData<List<UserTable>> {
        return userList
    }

    fun insertData(data: UserTable) {
        UserInsertion(userDAO).execute(data)
    }


    class UserInsertion(var userDAO: UserDAO) : AsyncTask<UserTable, Void, Void>() {


        override fun doInBackground(vararg params: UserTable): Void? {
            userDAO.deleteAllData()

            userDAO.insertDetails(params[0])
            return null
        }
    }

}