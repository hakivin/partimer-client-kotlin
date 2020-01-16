package com.szechuanstudio.partimer.ui.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.szechuanstudio.partimer.R
import com.szechuanstudio.partimer.ui.login.LoginActivity
import com.szechuanstudio.partimer.utils.PreferenceUtils
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity

class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        logout.setOnClickListener {
            startActivity<LoginActivity>()
            PreferenceUtils.reset(applicationContext)
            finish()
        }
    }
}
