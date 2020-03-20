package com.szechuanstudio.kolegahotel.ui.main.ui.profile

import android.content.Context
import com.szechuanstudio.kolegahotel.data.model.Model
import com.szechuanstudio.kolegahotel.data.retrofit.Api
import com.szechuanstudio.kolegahotel.utils.PreferenceUtils
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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