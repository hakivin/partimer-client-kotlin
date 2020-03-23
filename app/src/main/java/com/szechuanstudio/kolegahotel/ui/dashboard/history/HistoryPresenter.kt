package com.szechuanstudio.kolegahotel.ui.dashboard.history

import android.content.Context
import com.szechuanstudio.kolegahotel.data.model.Model
import com.szechuanstudio.kolegahotel.data.retrofit.Api
import com.szechuanstudio.kolegahotel.utils.PreferenceUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistoryPresenter(private val view: HistoryView,
                       private val api: Api,
                       private val context: Context
) {
    fun getJobHistory(page: Int? = 1){
        api.getJobsHistory(PreferenceUtils.getId(context), page, PreferenceUtils.getToken(context))
            .enqueue(object : Callback<Model.JobHistoryResponse>{
                override fun onFailure(call: Call<Model.JobHistoryResponse>, t: Throwable) {
                    view.reject("Check your internet connection")
                }

                override fun onResponse(
                    call: Call<Model.JobHistoryResponse>,
                    response: Response<Model.JobHistoryResponse>
                ) {
                    if (response.isSuccessful)
                        view.showJobHistory(response.body()?.jobs?.data)
                    else
                        view.reject("Something went wrong")
                }

            })
    }
}