package com.szechuanstudio.kolegahotel.ui.dashboard.accepted

import android.content.Context
import com.szechuanstudio.kolegahotel.data.model.Model
import com.szechuanstudio.kolegahotel.data.retrofit.Api
import com.szechuanstudio.kolegahotel.utils.PreferenceUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AcceptedPresenter(private val view: AcceptedView,
                        private val api: Api,
                        private val context: Context) {

    fun getAcceptedJobs(page: Int? = 1){
        api.getAcceptedJobs(PreferenceUtils.getId(context), page, PreferenceUtils.getToken(context))
            .enqueue(object : Callback<Model.JobsAcceptedResponse>{
                override fun onFailure(call: Call<Model.JobsAcceptedResponse>, t: Throwable) {
                    view.reject("Something went wrong")
                }

                override fun onResponse(
                    call: Call<Model.JobsAcceptedResponse>,
                    response: Response<Model.JobsAcceptedResponse>
                ) {
                    if (response.isSuccessful)
                        view.showAcceptedJobs(response.body()?.jobs?.data)
                    else
                        response.errorBody()?.string()?.let { view.reject(it) }
                }

            })
    }
}