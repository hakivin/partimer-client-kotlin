package com.szechuanstudio.kolegahotel.ui.job

import android.content.Context
import com.szechuanstudio.kolegahotel.data.retrofit.Api
import com.szechuanstudio.kolegahotel.utils.PreferenceUtils
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class JobDetailPresenter(private val view: JobDetailView,
                         private val api: Api,
                         private val context: Context) {

    fun applyJob(idJob : Int?){
        api.applyJob(idJob, PreferenceUtils.getToken(context))
            .enqueue(object : Callback<ResponseBody>{
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    view.error(t.message)
                }

                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful)
                        view.success()
                    else
                        view.reject()
                }
            })
    }
}