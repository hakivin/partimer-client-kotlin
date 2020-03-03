package com.szechuanstudio.kolegahotel.ui.main.ui.profile.documents

import android.content.Context
import android.net.Uri
import com.szechuanstudio.kolegahotel.data.model.Model
import com.szechuanstudio.kolegahotel.data.retrofit.Api
import com.szechuanstudio.kolegahotel.utils.PreferenceUtils
import com.szechuanstudio.kolegahotel.utils.Utils
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class DocumentPresenter(private val view: DocumentView,
                        private val api: Api,
                        private val context: Context
) {
    fun getDocuments(){
        api.getProfile(PreferenceUtils.getId(context))
            .enqueue(object : Callback<Model.ProfileResponse>{
                override fun onFailure(call: Call<Model.ProfileResponse>, t: Throwable) {

                }

                override fun onResponse(
                    call: Call<Model.ProfileResponse>,
                    response: Response<Model.ProfileResponse>
                ) {
                    if (response.isSuccessful){
                        view.showDocuments(response.body()?.profile?.get(0))
                    }
                }

            })
    }

    fun updateKtp(uri : Uri?){
        val file = File(Utils.getRealPathFromURI(context, uri))
        val requestFile =
            RequestBody.create(MediaType.parse("multipart/form-data"), file)
        val body =
            MultipartBody.Part.createFormData("image", file.name, requestFile)

        api.uploadKtp(PreferenceUtils.getId(context),PreferenceUtils.getToken(context),body)
            .enqueue(object : Callback<Model.Profile>{
                override fun onFailure(call: Call<Model.Profile>, t: Throwable) {
                    println(t.message)
                }

                override fun onResponse(
                    call: Call<Model.Profile>,
                    response: Response<Model.Profile>
                ) {
                    if (response.isSuccessful) {
                        view.imageUploaded()
                    }
                }
            })
    }

    fun updateSkck(uri : Uri?){
        val file = File(Utils.getRealPathFromURI(context, uri))
        val requestFile =
            RequestBody.create(MediaType.parse("multipart/form-data"), file)
        val body =
            MultipartBody.Part.createFormData("image", file.name, requestFile)

        api.uploadSkck(PreferenceUtils.getId(context),PreferenceUtils.getToken(context),body)
            .enqueue(object : Callback<Model.Profile>{
                override fun onFailure(call: Call<Model.Profile>, t: Throwable) {
                    println(t.message)
                }

                override fun onResponse(
                    call: Call<Model.Profile>,
                    response: Response<Model.Profile>
                ) {
                    if (response.isSuccessful) {
                        view.imageUploaded()
                    }
                }
            })
    }

    fun updateCertificate(uri : Uri?){
        val file = File(Utils.getRealPathFromURI(context, uri))
        val requestFile =
            RequestBody.create(MediaType.parse("multipart/form-data"), file)
        val body =
            MultipartBody.Part.createFormData("image", file.name, requestFile)

        api.uploadCertificate(PreferenceUtils.getId(context),PreferenceUtils.getToken(context),body)
            .enqueue(object : Callback<Model.Profile>{
                override fun onFailure(call: Call<Model.Profile>, t: Throwable) {
                    println(t.message)
                }

                override fun onResponse(
                    call: Call<Model.Profile>,
                    response: Response<Model.Profile>
                ) {
                    if (response.isSuccessful) {
                        view.imageUploaded()
                    }
                }
            })
    }

    fun updateCard(uri : Uri?){
        val file = File(Utils.getRealPathFromURI(context, uri))
        val requestFile =
            RequestBody.create(MediaType.parse("multipart/form-data"), file)
        val body =
            MultipartBody.Part.createFormData("image", file.name, requestFile)

        api.uploadCard(PreferenceUtils.getId(context),PreferenceUtils.getToken(context),body)
            .enqueue(object : Callback<Model.Profile>{
                override fun onFailure(call: Call<Model.Profile>, t: Throwable) {
                    println(t.message)
                }

                override fun onResponse(
                    call: Call<Model.Profile>,
                    response: Response<Model.Profile>
                ) {
                    if (response.isSuccessful) {
                        view.imageUploaded()
                    }
                }
            })
    }
}