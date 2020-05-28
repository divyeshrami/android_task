package com.example.clientdemo.retrofit

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

interface RetrofitHelper {

    @FormUrlEncoded
    @Headers("Content-Type:application/json","IMSI:357175048449937","IMEI:510110406068589")
    @POST("login")
    fun login(
        @Field("username") userName: String,
        @Field("password") password: String
    ): Call<ResponseBody>

}