package com.szechuanstudio.kolegahotel.ui.login

import android.content.Context
import com.szechuanstudio.kolegahotel.data.model.Model
import com.szechuanstudio.kolegahotel.data.retrofit.Api
import com.szechuanstudio.kolegahotel.utils.PreferenceUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginPresenter(private val view: LoginView,
                     private val api : Api,
                     private val context: Context) {
    fun login(email: String, password: String){

        api.login(email, password)
            .enqueue(object : Callback<Model.LoginObject>{
            override fun onFailure(call: Call<Model.LoginObject>, t: Throwable) {
                view.failed(t.message)
            }

            override fun onResponse(
                call: Call<Model.LoginObject>,
                response: Response<Model.LoginObject>
            ) {
                val user = response.body()?.login?.user
                if (user!=null) {
                    view.getUser(user)
                    PreferenceUtils.saveEmail(email, context)
                    PreferenceUtils.savePassword(password, context)
                    PreferenceUtils.saveToken(response.body()?.login?.access_token.toString(), context)
                    user.id?.let { PreferenceUtils.saveId(it, context) }
                }
                else
                    view.failed("Invalid Credential")
            }
        })
    }
}