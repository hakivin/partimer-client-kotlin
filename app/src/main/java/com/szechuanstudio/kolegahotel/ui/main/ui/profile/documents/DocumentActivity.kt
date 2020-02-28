package com.szechuanstudio.kolegahotel.ui.main.ui.profile.documents

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.szechuanstudio.kolegahotel.R
import kotlinx.android.synthetic.main.activity_document.*

class DocumentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_document)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Upload Documents"
        refresh_document.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimaryDark, R.color.colorIcon)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}
