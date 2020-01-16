package com.szechuanstudio.partimer.data.retrofit

import com.szechuanstudio.partimer.data.model.Model
import okhttp3.ResponseBody
import retrofit2.http.POST
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded

interface Api {

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String) : Call<Model.LoginResponse>

    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("password_confirmation") passwordConfirmation : String) : Call<Model.LoginResponse>
}

object RetrofitClient {

    fun create(): Api {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("http://192.168.0.104:8000/api/")
            .build()
        return retrofit.create(Api::class.java)
    }
}