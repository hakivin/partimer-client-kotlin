package com.szechuanstudio.kolegahotel.ui.main.ui.profile.update

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

class UpdateProfilePresenter(private val view: UpdateProfileView,
                             private val api: Api,
                             private val context: Context) {

    fun updateProfile(newProfile: Model.Profile){
        api.updateProfile(PreferenceUtils.getId(context), PreferenceUtils.getToken(context), newProfile)
            .enqueue(object : Callback<ResponseBody>{
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    view.failed()
                }

                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    println(response.errorBody()?.string())
                    view.success()
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
                .enqueue(object : Callback<Model.ProfileResponse>{
                    override fun onFailure(call: Call<Model.ProfileResponse>, t: Throwable) {
                        println(t.message)
                    }

                    override fun onResponse(
                        call: Call<Model.ProfileResponse>,
                        response: Response<Model.ProfileResponse>
                    ) {
                        println(response.code())
                        val profile = response.body()?.profile
                        if (profile != null) {
                            println(profile.foto)
                            view.getPhoto(profile)
                        }
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
            .enqueue(object : Callback<Model.ProfileResponse>{
                override fun onFailure(call: Call<Model.ProfileResponse>, t: Throwable) {
                    println(t.message)
                }

                override fun onResponse(
                    call: Call<Model.ProfileResponse>,
                    response: Response<Model.ProfileResponse>
                ) {
                    val profile = response.body()?.profile
                    if (profile != null) {
                        println(profile.cover)
                        view.getCover(profile)
                    }
                }
            })
    }
}