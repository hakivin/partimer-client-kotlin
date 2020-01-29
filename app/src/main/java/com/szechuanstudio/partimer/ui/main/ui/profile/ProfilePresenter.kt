package com.szechuanstudio.partimer.ui.main.ui.profile

import android.content.Context
import com.szechuanstudio.partimer.data.model.Model
import com.szechuanstudio.partimer.data.retrofit.Api
import com.szechuanstudio.partimer.utils.PreferenceUtils
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfilePresenter(private val view: ProfileView,
                       private val api: Api,
                       private val context: Context) {

    fun checkProfile(){
        api.getProfile(PreferenceUtils.getId(context))
            .enqueue(object : Callback<Model.ProfileResponse> {
                override fun onFailure(call: Call<Model.ProfileResponse>, t: Throwable) {
                    view.reject("Check your internet connection")
                }

                override fun onResponse(
                    call: Call<Model.ProfileResponse>,
                    response: Response<Model.ProfileResponse>
                ) {
                    val profile = response.body()?.profile?.get(0)
                    if (profile == null)
                        view.reject("Something went wrong")
                    else {
                        view.showProfile(profile)
                    }
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