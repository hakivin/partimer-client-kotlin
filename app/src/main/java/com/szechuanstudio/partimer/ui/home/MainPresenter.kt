package com.szechuanstudio.partimer.ui.home

import android.content.Context
import com.szechuanstudio.partimer.data.model.Model
import com.szechuanstudio.partimer.data.retrofit.Api
import com.szechuanstudio.partimer.utils.PreferenceUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainPresenter(private val view: MainView,
                    private val api: Api,
                    private val context: Context) {

    fun checkProfile(){
        val id = PreferenceUtils.getId(context)
        api.getProfile(id).enqueue(object : Callback<Model.ProfileResponse>{
            override fun onFailure(call: Call<Model.ProfileResponse>, t: Throwable) {
                t.message?.let { view.error(it) }
            }

            override fun onResponse(
                call: Call<Model.ProfileResponse>,
                response: Response<Model.ProfileResponse>
            ) {
                val profile = response.body()?.profile?.get(0)
                if (profile == null)
                    view.error("Something went wrong")
                else if (profile.nomor_telepon.isNullOrEmpty() || profile.alamat.isNullOrEmpty()){
                    view.reject(profile)
                }
            }
        })
    }
}