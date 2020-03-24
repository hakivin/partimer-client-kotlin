package com.szechuanstudio.kolegahotel.ui.main.ui.profile.update

import android.content.Context
import com.szechuanstudio.kolegahotel.data.model.Model
import com.szechuanstudio.kolegahotel.data.retrofit.Api
import com.szechuanstudio.kolegahotel.utils.PreferenceUtils
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
                    if (response.isSuccessful)
                        view.success()
                    else
                        view.failed()
                }
            })
    }
}