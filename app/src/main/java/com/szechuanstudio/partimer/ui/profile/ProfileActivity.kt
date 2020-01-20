package com.szechuanstudio.partimer.ui.profile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.szechuanstudio.partimer.R
import com.szechuanstudio.partimer.data.model.Model
import com.szechuanstudio.partimer.data.retrofit.RetrofitClient
import org.jetbrains.anko.toast

class ProfileActivity : AppCompatActivity(), ProfileView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val client = RetrofitClient.getInstance()
        val presenter = ProfilePresenter(this, client, applicationContext)
        presenter.getProfile()
    }

    override fun showProfile(profile: Model.Profile) {
        toast("Success")
    }

    override fun reject() {
        toast("Failed")
    }
}
