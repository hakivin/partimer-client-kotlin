package com.szechuanstudio.kolegahotel.ui.register

import android.content.Context
import android.util.Log
import com.google.gson.GsonBuilder
import com.szechuanstudio.kolegahotel.data.model.Model
import com.szechuanstudio.kolegahotel.data.retrofit.Api
import com.szechuanstudio.kolegahotel.utils.PreferenceUtils
import okhttp3.ResponseBody
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
            .enqueue(object : Callback<Model.LoginObject>{
                override fun onFailure(call: Call<Model.LoginObject>, t: Throwable) {
                    view.failed(t.message)
                    Log.d("TAG", "onFailure: ${t.message}")
                }

                override fun onResponse(
                    call: Call<Model.LoginObject>,
                    response: Response<Model.LoginObject>
                ) {
                    val newUser = response.body()?.login?.user
                    if (newUser!=null){
                        view.registered(newUser)
                        PreferenceUtils.saveEmail(email, context)
                        PreferenceUtils.savePassword(password, context)
                        PreferenceUtils.saveToken(response.body()?.login?.access_token.toString(), context)
                        newUser.id?.let { PreferenceUtils.saveId(it, context) }
                    } else if (response.code() == 422){
                        val gson = GsonBuilder().create()
                        val error = gson.fromJson(response.errorBody()?.string(), Model.ErrorResponse::class.java)
                        when {
                            error.errors?.name != null -> view.failed("The name has already been taken.")
                            error.errors?.email != null -> view.failed("The email has already been taken.")
                            error.errors?.password != null -> view.failed("The password confirmation does not match.")
                            else -> view.failed("Invalid Credential")
                        }
                        Log.d("TAG", "onResponse: ${response.errorBody()?.string()}")
                    }
                }
            })
//        api.registertest(name, email, password, confPassword).enqueue(object : Callback<ResponseBody>{
//            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
//
//            }
//
//            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
//                Log.d("TAG", "onResponse: ${response.body()?.string()}")
//            }
//
//        })
    }
}