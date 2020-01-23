package com.szechuanstudio.partimer.ui.main.ui.profile.update

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import com.szechuanstudio.partimer.data.model.Model
import com.szechuanstudio.partimer.data.retrofit.Api
import com.szechuanstudio.partimer.data.retrofit.RetrofitClient
import com.szechuanstudio.partimer.utils.PreferenceUtils
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
                    view.success()
                }
            })
    }

    fun uploadPhoto(uri : Uri?){
            val file = File(getRealPathFromURI(uri))
            val requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file)
            val body =
                MultipartBody.Part.createFormData("image", file.name, requestFile)

            RetrofitClient.getInstance().uploadPhoto(PreferenceUtils.getId(context),PreferenceUtils.getToken(context),body)
                .enqueue(object : Callback<Model.Profile>{
                    override fun onFailure(call: Call<Model.Profile>, t: Throwable) {
                        println(t.message)
                    }

                    override fun onResponse(
                        call: Call<Model.Profile>,
                        response: Response<Model.Profile>
                    ) {
                        val profile = response.body()
                        if (profile != null) {
                            view.getPhoto(profile)
                        }
                    }
                })
    }

    fun uploadCover(uri: Uri?){
        val file = File(getRealPathFromURI(uri))
        val requestFile =
            RequestBody.create(MediaType.parse("multipart/form-data"), file)
        val body =
            MultipartBody.Part.createFormData("image", file.name, requestFile)

        RetrofitClient.getInstance().uploadCover(PreferenceUtils.getId(context),PreferenceUtils.getToken(context),body)
            .enqueue(object : Callback<Model.Profile>{
                override fun onFailure(call: Call<Model.Profile>, t: Throwable) {
                    println(t.message)
                }

                override fun onResponse(
                    call: Call<Model.Profile>,
                    response: Response<Model.Profile>
                ) {
                    val profile = response.body()
                    if (profile != null) {
                        view.getCover(profile)
                    }
                }
            })
    }

    private fun getRealPathFromURI(contentURI: Uri?): String? {
        val result: String?
        val cursor =
            context.contentResolver.query(contentURI!!, null, null, null, null)
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.path
        } else {
            cursor.moveToFirst()
            val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            result = cursor.getString(idx)
            cursor.close()
        }
        return result
    }
}