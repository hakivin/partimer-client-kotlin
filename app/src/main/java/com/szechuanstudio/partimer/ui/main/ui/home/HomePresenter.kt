package com.szechuanstudio.partimer.ui.main.ui.home

import android.content.Context
import com.szechuanstudio.partimer.data.model.Model
import com.szechuanstudio.partimer.data.retrofit.Api
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomePresenter(private val view: HomeView,
                    private val api: Api,
                    private val context: Context) {
    fun getJobs(){
        api.getAllJobs()
            .enqueue(object : Callback<Model.JobsResponse>{
                override fun onFailure(call: Call<Model.JobsResponse>, t: Throwable) {
                    view.reject(t.message)
                }

                override fun onResponse(
                    call: Call<Model.JobsResponse>,
                    response: Response<Model.JobsResponse>
                ) {
                    if (!response.isSuccessful) {
                        view.reject("Error code = ${response.code()}")
                        return
                    }
                    view.showAllJobs(response.body()?.jobs)
                }

            })
    }
}