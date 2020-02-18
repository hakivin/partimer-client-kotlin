package com.szechuanstudio.kolegahotel.ui.main.ui.profile.positions

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.szechuanstudio.kolegahotel.R
import com.szechuanstudio.kolegahotel.data.model.Model
import com.szechuanstudio.kolegahotel.data.retrofit.RetrofitClient
import kotlinx.android.synthetic.main.activity_update_position.*

class UpdatePositionActivity : AppCompatActivity(), UpdatePositionView {

    private lateinit var presenter: UpdatePositionPresenter
    private lateinit var allPositions: List<Model.Position>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_position)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Edit Positions"
        presenter = UpdatePositionPresenter(this, RetrofitClient.getInstance(), applicationContext)
        presenter.getAllPositions()
    }

    override fun showAllPositions(positions: List<Model.Position>?) {
        if (positions != null) {
            allPositions = positions
        }
    }

    override fun populateUserPositions(userPositions: List<Model.Position>?) {
        rv_update_position.layoutManager = LinearLayoutManager(this)
        rv_update_position.adapter = userPositions?.let { UpdatePositionAdapter(applicationContext, allPositions, it) }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}
