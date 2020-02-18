package com.szechuanstudio.kolegahotel.ui.register

import android.content.Context
import com.szechuanstudio.kolegahotel.data.model.Model
import com.szechuanstudio.kolegahotel.data.retrofit.Api
import com.szechuanstudio.kolegahotel.utils.PreferenceUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterPresenter(private val view : RegisterView,
                        private val api : Api,
                        private val context: Context) {
    fun register(name: String,
                 email: String,
                 password: String,
                 confPassword: String) {

        api.register(name, email, password, confPassword)
            .enqueue(object : Callback<Model.LoginResponse>{
                override fun onFailure(call: Call<Model.LoginResponse>, t: Throwable) {
                    view.failed(null)
                }

                override fun onResponse(
                    call: Call<Model.LoginResponse>,
                    response: Response<Model.LoginResponse>
                ) {
                    val newUser = response.body()?.user
                    println("test ${newUser?.name}")
                    if (newUser!=null){
                        view.registered(newUser)
                        PreferenceUtils.saveEmail(email, context)
                        PreferenceUtils.savePassword(password, context)
                        PreferenceUtils.saveToken(response.body()!!.access_token.toString(), context)
                        newUser.id?.let { PreferenceUtils.saveId(it, context) }
                    } else
                        view.failed("Invalid Credential")
                }

            })
    }
}