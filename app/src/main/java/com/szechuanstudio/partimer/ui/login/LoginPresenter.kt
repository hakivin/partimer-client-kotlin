package com.szechuanstudio.partimer.ui.login

import android.content.Context
import com.szechuanstudio.partimer.data.model.Model
import com.szechuanstudio.partimer.data.retrofit.Api
import com.szechuanstudio.partimer.utils.PreferenceUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginPresenter(private val view: LoginView,
                     private val api : Api,
                     private val context: Context) {
    fun login(email: String, password: String){

        api.login(email, password)
            .enqueue(object : Callback<Model.LoginResponse>{
            override fun onFailure(call: Call<Model.LoginResponse>, t: Throwable) {
                view.failed(null)
            }

            override fun onResponse(
                call: Call<Model.LoginResponse>,
                response: Response<Model.LoginResponse>
            ) {
                val user = response.body()?.user
                if (user!=null) {
                    view.getUser(user)
                    PreferenceUtils.saveEmail(email, context)
                    PreferenceUtils.savePassword(password, context)
                    PreferenceUtils.saveToken(response.body()!!.access_token.toString(), context)
                    user.id?.let { PreferenceUtils.saveId(it, context) }
                }
                else
                    view.failed("Invalid Credential")
            }
        })
    }
}