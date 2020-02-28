package com.szechuanstudio.kolegahotel.ui.main.ui.dashboard

import android.content.Context
import com.szechuanstudio.kolegahotel.data.model.Model
import com.szechuanstudio.kolegahotel.data.retrofit.Api
import com.szechuanstudio.kolegahotel.utils.PreferenceUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashboardPresenter(private val view: DashboardView,
                         private val api: Api,
                         private val context: Context
) {
    fun getUserJob(){
        api.getUserJob(PreferenceUtils.getId(context))
            .enqueue(object : Callback<Model.JobsResponse>{
                override fun onFailure(call: Call<Model.JobsResponse>, t: Throwable) {

                }

                override fun onResponse(
                    call: Call<Model.JobsResponse>,
                    response: Response<Model.JobsResponse>
                ) {

                }

            })
    }

    fun getJobsDetail(){
        api.getJobDetail(PreferenceUtils.getId(context), PreferenceUtils.getToken(context))
            .enqueue(object : Callback<Model.JobDetailResponse>{
                override fun onFailure(call: Call<Model.JobDetailResponse>, t: Throwable) {

                }

                override fun onResponse(
                    call: Call<Model.JobDetailResponse>,
                    response: Response<Model.JobDetailResponse>
                ) {

                }

            })
    }
}