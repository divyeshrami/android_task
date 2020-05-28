package com.example.clientdemo.login.model

import com.google.gson.annotations.SerializedName

data class BaseResponseModel(
    val erorCode: String,
    val errorMessage: String,
    @SerializedName("user") val userResponseModel: UserResponseModel
)