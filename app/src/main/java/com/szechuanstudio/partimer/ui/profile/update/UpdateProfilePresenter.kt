package com.szechuanstudio.partimer.ui.profile.update

import android.content.Context
import com.szechuanstudio.partimer.data.model.Model
import com.szechuanstudio.partimer.data.retrofit.Api
import com.szechuanstudio.partimer.utils.PreferenceUtils
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
}