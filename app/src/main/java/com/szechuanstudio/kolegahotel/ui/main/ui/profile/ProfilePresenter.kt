package com.szechuanstudio.kolegahotel.ui.main.ui.profile

import android.content.Context
import android.net.Uri
import com.szechuanstudio.kolegahotel.data.model.Model
import com.szechuanstudio.kolegahotel.data.retrofit.Api
import com.szechuanstudio.kolegahotel.utils.PreferenceUtils
import com.szechuanstudio.kolegahotel.utils.Utils
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class ProfilePresenter(private val view: ProfileView,
                       private val api: Api,
                       private val context: Context) {

    fun checkProfile(){
        getPositions()
        api.getProfile(PreferenceUtils.getId(context), PreferenceUtils.getToken(context))
            .enqueue(object : Callback<Model.ProfileResponse> {
                override fun onFailure(call: Call<Model.ProfileResponse>, t: Throwable) {
                    view.reject("Check your internet connection")
                }

                override fun onResponse(
                    call: Call<Model.ProfileResponse>,
                    response: Response<Model.ProfileResponse>
                ) {
                    val profile = response.body()?.profile
                    if (profile == null)
                        view.reject("Something went wrong")
                    else {
                        view.showProfile(profile)
                    }
                }
            })
    }

    fun uploadPhoto(uri : Uri?){
        val file = File(Utils.getRealPathFromURI(context, uri))
        val requestFile =
            RequestBody.create(MediaType.parse("multipart/form-data"), file)
        val body =
            MultipartBody.Part.createFormData("image", file.name, requestFile)

        api.uploadPhoto(PreferenceUtils.getId(context),PreferenceUtils.getToken(context),body)
            .enqueue(object : Callback<ResponseBody>{
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    println(t.message)
                }

                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful)
                        view.reload()
                    else
                        view.reject("Error code: ${response.code()}")
                }
            })
    }

    fun uploadCover(uri: Uri?){
        val file = File(Utils.getRealPathFromURI(context, uri))
        val requestFile =
            RequestBody.create(MediaType.parse("multipart/form-data"), file)
        val body =
            MultipartBody.Part.createFormData("image", file.name, requestFile)

        api.uploadCover(PreferenceUtils.getId(context),PreferenceUtils.getToken(context),body)
            .enqueue(object : Callback<ResponseBody>{
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    println(t.message)
                }

                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful)
                        view.reload()
                    else
                        view.reject("Error code: ${response.code()}")
                }
            })
    }

    private fun getPositions(){
        api.getUserPositions(PreferenceUtils.getId(context))
            .enqueue(object : Callback<Model.PositionsResponse>{
                override fun onFailure(call: Call<Model.PositionsResponse>, t: Throwable) {
                    view.reject("Check your internet connection")
                }

                override fun onResponse(
                    call: Call<Model.PositionsResponse>,
                    response: Response<Model.PositionsResponse>
                ) {
                    view.showPositions(response.body())
                }

            })
    }

    fun logout(){
        api.logout(PreferenceUtils.getToken(context)).enqueue(object : Callback<ResponseBody>{
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                view.logoutFailed()
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.code() == 200)
                    view.logoutSuccess()
                else
                    view.logoutFailed()
            }
        })
    }
}