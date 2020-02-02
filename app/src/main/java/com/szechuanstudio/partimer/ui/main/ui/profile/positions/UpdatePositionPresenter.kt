package com.szechuanstudio.partimer.ui.main.ui.profile.positions

import android.content.Context
import com.szechuanstudio.partimer.data.model.Model
import com.szechuanstudio.partimer.data.retrofit.Api
import com.szechuanstudio.partimer.utils.PreferenceUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdatePositionPresenter(private val view: UpdatePositionView,
                              private val api: Api,
                              private val context: Context) {

    fun getAllPositions(){
        api.getPositions().enqueue(object : Callback<Model.PositionsResponse>{
            override fun onFailure(call: Call<Model.PositionsResponse>, t: Throwable) {

            }

            override fun onResponse(
                call: Call<Model.PositionsResponse>,
                response: Response<Model.PositionsResponse>
            ) {
                getUserPositions()
                view.showAllPositions(response.body()?.positions)
            }
        })
    }

    fun getUserPositions(){
        api.getUserPositions(PreferenceUtils.getId(context)).enqueue(object :
            Callback<Model.PositionsResponse>{
            override fun onFailure(call: Call<Model.PositionsResponse>, t: Throwable) {

            }

            override fun onResponse(
                call: Call<Model.PositionsResponse>,
                response: Response<Model.PositionsResponse>
            ) {
                view.populateUserPositions(response.body()?.positions)
            }

        })
    }
}