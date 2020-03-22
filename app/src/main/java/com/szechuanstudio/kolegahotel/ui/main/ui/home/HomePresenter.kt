package com.szechuanstudio.kolegahotel.ui.main.ui.home

import android.content.Context
import com.szechuanstudio.kolegahotel.data.model.Model
import com.szechuanstudio.kolegahotel.data.retrofit.Api
import com.szechuanstudio.kolegahotel.utils.PreferenceUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomePresenter(private val view: HomeView,
                    private val api: Api,
                    private val context: Context) {
    fun getJobs(page: Int?){
        api.getAllJobs(page, PreferenceUtils.getToken(context))
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
                    if (page == 1)
                        view.showAllJobs(response.body()?.jobs)
                    else
                        view.addJobs(response.body()?.jobs)
                }

            })
    }

    fun searchJob(query: String?){
        api.searchJobs(query, PreferenceUtils.getToken(context))
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

    fun searchJobWithPosition(id: Int?){
        api.getJobsWithPosition(id, PreferenceUtils.getToken(context))
            .enqueue(object : Callback<Model.JobsResponse>{
                override fun onFailure(call: Call<Model.JobsResponse>, t: Throwable) {
                    view.reject("Check your internet connection")
                }

                override fun onResponse(
                    call: Call<Model.JobsResponse>,
                    response: Response<Model.JobsResponse>
                ) {
                    if (response.isSuccessful)
                        view.showAllJobs(response.body()?.jobs)
                }
            })
    }

    fun setEmptyJob(){
        val emptyJob = emptyList<Model.JobData>()
        val emptyPaginate = Model.JobPaginate(1, emptyJob, 1)
        view.showAllJobs(emptyPaginate)
    }

    fun getPositions(){
        api.getPositions()
            .enqueue(object : Callback<Model.PositionsResponse>{
                override fun onFailure(call: Call<Model.PositionsResponse>, t: Throwable) {

                }

                override fun onResponse(
                    call: Call<Model.PositionsResponse>,
                    response: Response<Model.PositionsResponse>
                ) {
                    if (response.isSuccessful){
                        view.showPositions(response.body()?.positions)
                    }
                }
            })
    }

    fun getPendingJobs(){
        api.getAppliedJobs(PreferenceUtils.getId(context), PreferenceUtils.getToken(context))
            .enqueue(object : Callback<Model.JobsResponse>{
                override fun onFailure(call: Call<Model.JobsResponse>, t: Throwable) {
                    view.reject(t.message)
                }

                override fun onResponse(
                    call: Call<Model.JobsResponse>,
                    response: Response<Model.JobsResponse>
                ) {
                    if (response.isSuccessful)
                        view.showAllJobs(response.body()?.jobs)
                    else
                        view.reject(response.errorBody()?.string())
                }

            })
    }
}