package com.szechuanstudio.partimer.ui.main.ui.profile.update

import android.os.Bundle
import android.view.View
import android.widget.RadioButton
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
        setGender(profile)
        setEducation(profile?.pendidikan_terakhir)
        if (profile?.foto.isNullOrEmpty())
            Picasso.with(applicationContext).load(R.drawable.placeholder_avatar).noFade().into(update_photo)
    }

    private fun setGender(profile: Model.Profile?) {
        if (profile?.jenis_kelamin.equals("L")) {
            male = true
            update_gender_male.isChecked = true
        } else if (profile?.jenis_kelamin.equals("P")) {
            male = false
            update_gender_female.isChecked = true
        }
    }

    private fun constructModel(profile: Model.Profile?): Model.Profile? {
        profile?.nama = edit_update_name.text.toString()
        profile?.nama_lengkap = edit_update_full_name.text.toString()
        profile?.alamat = edit_update_address.text.toString()
        profile?.social_media = edit_update_social_media.text.toString()
        profile?.nomor_telepon = edit_update_phone_number.text.toString()
        profile?.jenis_kelamin = getGender()
        profile?.pendidikan_terakhir = getEducation()
        return profile
    }

    override fun success() {
        toast("success")
        finish()
    }

    override fun failed() {
        toast("failed")
    }

    private var male : Boolean? = null

    private fun getGender() : String?{
        return if (male == null)
            ""
        else {
            if (male as Boolean)
                "L"
            else
                "P"
        }
    }

    private fun getEducation() : String?{
        return when (spinner_update_latest_education.selectedItemPosition) {
            0 -> "SD"
            1 -> "SMP"
            2 -> "SMA"
            3 -> "Diploma"
            4 -> "S1"
            5 -> "S2"
            6 -> "S3"
            else -> null
        }
    }

    private fun setEducation(level : String?){
        when {
            level.equals("SD") -> spinner_update_latest_education.setSelection(0)
            level.equals("SMP") -> spinner_update_latest_education.setSelection(1)
            level.equals("SMA") -> spinner_update_latest_education.setSelection(2)
            level.equals("Diploma") -> spinner_update_latest_education.setSelection(3)
            level.equals("S1") -> spinner_update_latest_education.setSelection(4)
            level.equals("S2") -> spinner_update_latest_education.setSelection(5)
            level.equals("S3") -> spinner_update_latest_education.setSelection(6)
        }
    }

    fun onRadioButtonClicked(view: View) {
        if (view is RadioButton) {
            // Is the button now checked?
            val checked = view.isChecked

            // Check which radio button was clicked
            when (view.getId()) {
                update_gender_male.id ->
                    if (checked) {
                        male = true
                    }
                update_gender_female.id ->
                    if (checked) {
                        male = false
                    }
            }
        }
    }
}
