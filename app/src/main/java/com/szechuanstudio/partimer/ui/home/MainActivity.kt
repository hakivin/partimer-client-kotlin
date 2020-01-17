package com.szechuanstudio.partimer.ui.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.szechuanstudio.partimer.R
import com.szechuanstudio.partimer.data.model.Model
import com.szechuanstudio.partimer.data.retrofit.Api
import com.szechuanstudio.partimer.data.retrofit.RetrofitClient
import com.szechuanstudio.partimer.ui.login.LoginActivity
import com.szechuanstudio.partimer.utils.PreferenceUtils
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.ResponseBody
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.singleTop
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private var id : Int? = null
    private lateinit var client: Api

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        id = PreferenceUtils.getId(applicationContext)
        activateButton()
        client = RetrofitClient.create()
        client.getProfile(id).enqueue(object: Callback<Model.ProfileResponse>{
            override fun onFailure(call: Call<Model.ProfileResponse>, t: Throwable) {

            }

            override fun onResponse(
                call: Call<Model.ProfileResponse>,
                response: Response<Model.ProfileResponse>
            ) {
                val profile = response.body()?.profile?.get(0)
                profile?.nama = "Haki".also { profile?.nama_lengkap = profile?.nama }
                updateProfile(profile)
            }

        })
    }

    private fun activateButton() {
        logout.setOnClickListener {
            startActivity(intentFor<LoginActivity>().singleTop())
            PreferenceUtils.reset(applicationContext)
            finish()
        }
    }

    private fun updateProfile(newProfile: Model.Profile?){
        if (newProfile != null) {
            client.updateProfile(id, newProfile).enqueue(object : Callback<ResponseBody>{
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    toast("Update failed")
                }

                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    println(response.body()?.string())
                }

            })
        }
    }
}
