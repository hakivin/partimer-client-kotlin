package com.szechuanstudio.partimer.ui.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.szechuanstudio.partimer.R
import com.szechuanstudio.partimer.data.model.Model
import com.szechuanstudio.partimer.data.retrofit.RetrofitClient
import com.szechuanstudio.partimer.ui.login.LoginActivity
import com.szechuanstudio.partimer.ui.profile.update.UpdateProfileActivity
import com.szechuanstudio.partimer.utils.Constant
import com.szechuanstudio.partimer.utils.PreferenceUtils
import kotlinx.android.synthetic.main.activity_home.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.longToast
import org.jetbrains.anko.singleTop
import org.jetbrains.anko.toast

class HomeActivity : AppCompatActivity(), HomeView {

    private var id : Int? = null
    private lateinit var presenter: HomePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        id = PreferenceUtils.getId(applicationContext)
        activateButton()
        presenter = HomePresenter(this, RetrofitClient.getInstance(), applicationContext)
        presenter.checkProfile()
    }

    private fun activateButton() {
        logout.setOnClickListener {
            startActivity(intentFor<LoginActivity>().singleTop())
            PreferenceUtils.reset(applicationContext)
            finish()
        }
    }

    override fun showJobs() {

    }

    override fun reject(profile: Model.Profile) {
        startActivity(intentFor<UpdateProfileActivity>(Constant.KEY_PROFILE to profile).singleTop())
        toast("Please complete your identities first")
        finish()
    }

    override fun error(message: String) {
        longToast(message)
        finish()
    }
}
