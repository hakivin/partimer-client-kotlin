package com.szechuanstudio.partimer.ui.profile.update

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import com.szechuanstudio.partimer.R
import com.szechuanstudio.partimer.data.model.Model
import com.szechuanstudio.partimer.data.retrofit.RetrofitClient
import com.szechuanstudio.partimer.utils.Constant
import kotlinx.android.synthetic.main.activity_update_profile.*
import org.jetbrains.anko.toast

class UpdateProfileActivity : AppCompatActivity(), UpdateProfileView {

    private lateinit var presenter: UpdateProfilePresenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_profile)
        presenter = UpdateProfilePresenter(this, RetrofitClient.getInstance(), applicationContext)
        val profile = intent.getParcelableExtra<Model.Profile>(Constant.KEY_PROFILE)
        fillEditText(profile)
        btn_save_profile.setOnClickListener { _: View? ->
            val newProfile = constructModel(profile)
            newProfile?.let { presenter.updateProfile(it) }
        }
    }

    private fun fillEditText(profile: Model.Profile?) {
        edit_update_name.setText(profile?.nama)
        edit_update_full_name.setText(profile?.nama_lengkap)
        edit_update_address.setText(profile?.alamat)
        edit_update_social_media.setText(profile?.social_media)
        edit_update_phone_number.setText(profile?.nomor_telepon)
        if (profile?.foto.isNullOrEmpty())
            Picasso.with(applicationContext).load(R.drawable.placeholder_avatar).noFade().into(update_photo)
    }

    private fun constructModel(profile: Model.Profile?): Model.Profile? {
        profile?.nama = edit_update_name.text.toString()
        profile?.nama_lengkap = edit_update_full_name.text.toString()
        profile?.alamat = edit_update_address.text.toString()
        profile?.social_media = edit_update_social_media.text.toString()
        profile?.nomor_telepon = edit_update_phone_number.text.toString()
        return profile
    }

    override fun success() {
        toast("success")
        finish()
    }

    override fun failed() {
        toast("failed")
    }
}
