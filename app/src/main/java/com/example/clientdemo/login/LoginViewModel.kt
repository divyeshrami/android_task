package com.example.clientdemo.login

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.clientdemo.R
import com.example.clientdemo.login.model.BaseResponseModel
import com.example.clientdemo.login.model.LoginErrorModel
import com.example.clientdemo.login.model.UserResponseModel
import com.example.clientdemo.retrofit.RetrofitClient
import com.example.clientdemo.retrofit.RetrofitHelper
import com.example.clientdemo.room.UserTable
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginViewModel(application: Application) : AndroidViewModel(application),
    Callback<ResponseBody> {

    var btnSelected: MutableLiveData<Boolean> = MutableLiveData(false)
    var name = MutableLiveData<String>()
    var nameErorr = MutableLiveData<String>()
    var password = MutableLiveData<String>()
    var passwordError = MutableLiveData<String>()
    var loginLiveData = MutableLiveData<UserResponseModel>()
    var loginRepository: LoginRepository

    init {
        name.value = ""
        password.value = ""
//        name.value = "username"
//        password.value = "1111111"
        loginRepository = LoginRepository(application)
    }

    fun onUserNameChange(s: CharSequence, start: Int, befor: Int, count: Int) {
        name.value = s.toString()

        /*
        * Providing error to textEdit*/
        nameErorr.value = validateUserName(s.toString())

        /*
        * setting login button visiblity*/
        btnSelected.value =
            (name.value.toString().length > 0 && password.value.toString().length > 0)
    }


    fun onPasswordChanged(s: CharSequence, start: Int, befor: Int, count: Int) {
        password.value = s.toString()

        /*
        * Providing error to textEdit*/
        passwordError.value = validatePassword(s.toString())

        /*
        * setting login button visiblity*/
        btnSelected.value =
            (name.value.toString().length > 0 && password.value.toString().length > 0)
    }


    fun login() {

        if (!isNetworkConnected()) {
            Toast.makeText(getApplication(), "No internet available", Toast.LENGTH_LONG).show()
            return
        }

        RetrofitClient.client.create(RetrofitHelper::class.java)
            .login(userName = name.value!!, password = password.value!!)
            .enqueue(this)
    }

    override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
    }

    override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {

        /*
        * Get header from response*/

        val xAcc = response!!.headers().get("X-Acc")
        Log.i("Header", "Header Name $xAcc")


        val body = response!!.body()

        val response = body.string()

        val json = JSONObject(response)

        val code = json.getString("errorCode")

        Log.i("CODE", "Response code : $code")

        val gson = Gson()

        when (code) {
            "00" -> {
                val type = object : TypeToken<BaseResponseModel>() {}.type
                val baseResponse = gson.fromJson(response, type) as BaseResponseModel

                Log.i("CODE", "Response code 00 : ${baseResponse.errorMessage}")

                var userTale = UserTable(
                    baseResponse.userResponseModel.userId.toInt(),
                    baseResponse.userResponseModel.userName,
                    xAcc
                )

                loginLiveData.postValue(baseResponse.userResponseModel)

                //Insert to local database
                insertToLocal(userTale)

            }
            "01" -> {
                val type = object : TypeToken<LoginErrorModel>() {}.type
                val baseResponse = gson.fromJson(response, type) as LoginErrorModel

                Log.i("CODE", "Response code 01 : ${baseResponse.errorMessage}")
            }
        }

    }


    fun insertToLocal(userTable: UserTable) {
        loginRepository.insertData(userTable)
    }

    private fun validateUserName(s: String): String? {
        when {
            s.isEmpty() -> {
                return getApplication<Application>().resources.getString(R.string.please_enter_username)
            }
            s.length > 30 -> {
                return getApplication<Application>().resources.getString(R.string.enter_username_below_thirty)
            }
            else -> {
                return null
            }
        }
    }

    private fun validatePassword(s: String): String? {
        when {
            s.isEmpty() -> {
                return getApplication<Application>().resources.getString(R.string.please_enter_password)
            }
            s.length > 30 -> {
                return getApplication<Application>().resources.getString(R.string.enter_password_below_sixteen)
            }
            else -> {
                return null
            }
        }
    }

    private fun isNetworkConnected(): Boolean {
        val cm =
            getApplication<Application>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        return cm!!.activeNetworkInfo != null && cm.activeNetworkInfo.isConnected
    }

}