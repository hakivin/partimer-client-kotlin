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
    fun getJobs(page: Int? = 1){
        api.getAllJobs(page, PreferenceUtils.getToken(context))
            .enqueue(object : Callback<Model.JobsResponse>{
                override fun onFailure(call: Call<Model.JobsResponse>, t: Throwable) {
                    view.reject("Check your internet connection")
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

    fun searchJob(query: String?, page: Int? = 1){
        api.searchJobs(query, page,PreferenceUtils.getToken(context))
            .enqueue(object : Callback<Model.JobsResponse>{
                override fun onFailure(call: Call<Model.JobsResponse>, t: Throwable) {
                    view.reject("Check your internet connection")
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
                        view.showSearchedJobs(response.body()?.jobs, query)
                    else
                        view.addSearchedJobs(response.body()?.jobs, query)
                }
            })
    }

    fun searchJobWithPosition(id: Int?, page: Int? = 1){
        api.getJobsWithPosition(id, PreferenceUtils.getToken(context))
            .enqueue(object : Callback<Model.JobsResponse>{
                override fun onFailure(call: Call<Model.JobsResponse>, t: Throwable) {
                    view.reject("Check your internet connection")
                }

                override fun onResponse(
                    call: Call<Model.JobsResponse>,
                    response: Response<Model.JobsResponse>
                ) {
                    if (!response.isSuccessful){
                        view.reject("Error code = ${response.code()}")
                        return
                    }
                    if (page == 1)
                        view.showPositionJobs(response.body()?.jobs, id)
                    else
                        view.addPositionJobs(response.body()?.jobs, id)
                }
            })
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

    fun getPendingJobs(page: Int? = 1){
        api.getAppliedJobs(PreferenceUtils.getId(context), page, PreferenceUtils.getToken(context))
            .enqueue(object : Callback<Model.JobsResponse>{
                override fun onFailure(call: Call<Model.JobsResponse>, t: Throwable) {
                    view.reject("Check your internet connection")
                }

                override fun onResponse(
                    call: Call<Model.JobsResponse>,
                    response: Response<Model.JobsResponse>
                ) {
                    if (response.isSuccessful) {
                        if (page == 1)
                            view.showPendingJobs(response.body()?.jobs)
                        else
                            view.addPendingJobs(response.body()?.jobs)
                    }
                    else
                        view.reject(response.errorBody()?.string())
                }

            })
    }
}