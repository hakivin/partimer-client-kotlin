package com.szechuanstudio.kolegahotel.data.retrofit

import com.szechuanstudio.kolegahotel.BuildConfig
import com.szechuanstudio.kolegahotel.data.model.Model
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

    @Multipart
    @POST("profile/{id}/upload/ktp")
    fun uploadKtp(@Path("id") id: Int?, @Header("Authorization") token : String?, @Part cover:MultipartBody.Part) : Call<Model.Profile>

    @Multipart
    @POST("profile/{id}/upload/skck")
    fun uploadSkck(@Path("id") id: Int?, @Header("Authorization") token : String?, @Part cover:MultipartBody.Part) : Call<Model.Profile>

    @Multipart
    @POST("profile/{id}/upload/sertif")
    fun uploadCertificate(@Path("id") id: Int?, @Header("Authorization") token : String?, @Part cover:MultipartBody.Part) : Call<Model.Profile>

    @Multipart
    @POST("profile/{id}/upload/kartu")
    fun uploadCard(@Path("id") id: Int?, @Header("Authorization") token : String?, @Part cover:MultipartBody.Part) : Call<Model.Profile>

    @GET("positions")
    fun getPositions() : Call<Model.PositionsResponse>

    @GET("profile/{id}/positions")
    fun getUserPositions(@Path("id") id: Int?) : Call<Model.PositionsResponse>

    @POST("profile/positions/update")
    fun selectPosition(@Query("id") id: Int?, @Header("Authorization") token : String?) : Call<ResponseBody>

    @GET("auth/jobs")
    fun getAllJobs(@Header("Authorization") token : String?) : Call<Model.JobsResponse>

    @GET("auth/jobs/{query}")
    fun searchJobs(@Path("query") query: String?, @Header("Authorization") token : String?) : Call<Model.JobsResponse>

    @GET("auth/jobs/position/{id}")
    fun getJobsWithPosition(@Path("id") idPosition: Int?, @Header("Authorization") token : String?) : Call<Model.JobsResponse>

    @POST("auth/jobs/{job}/apply")
    fun applyJob(@Path("job") idJob: Int?, @Header("Authorization") token : String?) : Call<ResponseBody>

    @GET("profile/{id}/activejobs")
    fun getActiveJob(@Path("id") id: Int?, @Header("Authorization") token : String?) : Call<Model.JobDetailResponse>

    @GET("profile/{id}/appliedjobs")
    fun getAppliedJobs(@Path("id") id: Int?, @Header("Authorization") token : String?) : Call<Model.JobsResponse>

    @GET("profile/{id}/acceptedjobs")
    fun getAcceptedJobs(@Path("id") id: Int?, @Header("Authorization") token : String?) : Call<Model.JobsAcceptedResponse>

    @GET("profile/{id}/jobhistory")
    fun getJobsHistory(@Path("id") id: Int?) : Call<Model.JobHistoryResponse>

    @GET("auth/job/{id}/todo")
    fun getCheckedTodolist(@Path("id") jobId: Int?, @Header("Authorization") token : String?) : Call<Model.ToDoListResponse>

    @POST("auth/jobs/todo/{todo}/check")
    fun checkTodolist(@Path("todo") todoId: Int?, @Header("Authorization") token : String?) : Call<ResponseBody>
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