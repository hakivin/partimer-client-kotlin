package com.szechuanstudio.partimer.ui.profile

import android.content.Context
import com.szechuanstudio.partimer.data.model.Model
import com.szechuanstudio.partimer.data.retrofit.Api
import com.szechuanstudio.partimer.utils.PreferenceUtils
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfilePresenter(val view: ProfileView,
                       val api: Api,
                       val context: Context) {

    fun getProfile(){
        api.getProfile(PreferenceUtils.getId(context)).enqueue(object :
            Callback<Model.ProfileResponse>{
            override fun onFailure(call: Call<Model.ProfileResponse>, t: Throwable) {
                view.reject()
            }

            override fun onResponse(
                call: Call<Model.ProfileResponse>,
                response: Response<Model.ProfileResponse>
            ) {
                val profile = response.body()?.profile?.get(0)
                if (profile != null) {
                    view.showProfile(profile)
                }
            }

        })
    }

    fun update(profile: Model.Profile){
        api.updateProfile(PreferenceUtils.getId(context), PreferenceUtils.getToken(context), profile).enqueue(object :
            Callback<ResponseBody>{
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                println("err:" + t.message + t.stackTrace)
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                println("suc:" + response.body()?.string())
            }
        })
    }
}