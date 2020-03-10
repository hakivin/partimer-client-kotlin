package com.szechuanstudio.kolegahotel.ui.dashboard.active

import android.content.Context
import com.szechuanstudio.kolegahotel.data.model.Model
import com.szechuanstudio.kolegahotel.data.retrofit.Api
import com.szechuanstudio.kolegahotel.utils.PreferenceUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ActivePresenter(private val view: ActiveView,
                      private val api: Api,
                      private val context: Context
) {
    fun getActiveJobs(){
        api.getActiveJob(PreferenceUtils.getId(context), PreferenceUtils.getToken(context))
            .enqueue(object : Callback<Model.JobDetailResponse>{
                override fun onFailure(call: Call<Model.JobDetailResponse>, t: Throwable) {
                    view.reject(t.message)
                }

                override fun onResponse(
                    call: Call<Model.JobDetailResponse>,
                    response: Response<Model.JobDetailResponse>
                ) {
                    if (response.isSuccessful)
                        view.showJobs(response.body()?.active_jobs)
                    else
                        view.reject(response.errorBody()?.string())
                }

            })
    }

    fun getCheckedTodolist(jobId: Int?){
        api.getCheckedTodolist(jobId, PreferenceUtils.getToken(context))
            .enqueue(object : Callback<Model.ToDoListResponse>{
                override fun onFailure(call: Call<Model.ToDoListResponse>, t: Throwable) {

                }

                override fun onResponse(
                    call: Call<Model.ToDoListResponse>,
                    response: Response<Model.ToDoListResponse>
                ) {
                    if (response.isSuccessful){
                        view.showCheckedTodolist(response.body()?.todolist)
                    }
                    println(response.code())
                }
            })
    }
}