package com.szechuanstudio.partimer.data.retrofit

import com.szechuanstudio.partimer.BuildConfig
import com.szechuanstudio.partimer.data.model.Model
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

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

    @POST("logout")
    fun logout(@Header("Authorization") token : String?) : Call<ResponseBody>

    @GET("profile/{id}")
    fun getProfile(@Path("id") id : Int?) : Call<Model.ProfileResponse>

    @PUT("profile/{id}/update")
    fun updateProfile(@Path("id") id : Int?, @Header("Authorization") token : String?, @Body updatedProfile: Model.Profile) : Call<ResponseBody>

    @Multipart
    @POST("profile/{id}/upload/profile")
    fun uploadPhoto(@Path("id") id: Int?, @Header("Authorization") token : String?, @Part imageName:MultipartBody.Part) : Call<Model.Profile>

    @Multipart
    @POST("profile/{id}/upload/cover")
    fun uploadCover(@Path("id") id: Int?, @Header("Authorization") token : String?, @Part cover:MultipartBody.Part) : Call<Model.Profile>

    @GET("positions")
    fun getPositions() : Call<Model.PositionsResponse>
}

class RetrofitClient {

    companion object {
        private var instance : Api? = null

        @Synchronized
        fun getInstance(): Api {
            if (instance == null)
                instance = Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(BuildConfig.BASE_URL+"/api/")
                    .build()
                    .create(Api::class.java)
            return instance as Api
        }
    }
}