package com.szechuanstudio.partimer.ui.main.ui.profile.update

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.dhaval2404.imagepicker.ImagePicker
import com.squareup.picasso.Picasso
import com.szechuanstudio.partimer.BuildConfig
import com.szechuanstudio.partimer.R
import com.szechuanstudio.partimer.data.model.Model
import com.szechuanstudio.partimer.data.retrofit.RetrofitClient
import com.szechuanstudio.partimer.utils.Constant
import kotlinx.android.synthetic.main.activity_update_profile.*
import org.jetbrains.anko.toast
import pub.devrel.easypermissions.EasyPermissions
import java.util.*

class UpdateProfileActivity : AppCompatActivity(), UpdateProfileView {

    private lateinit var presenter: UpdateProfilePresenter
    private lateinit var profile : Model.Profile
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_profile)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close_black_24dp)
        supportActionBar?.title = "Edit Profile"
        presenter = UpdateProfilePresenter(this, RetrofitClient.getInstance(), applicationContext)
        profile = intent.getParcelableExtra(Constant.KEY_PROFILE)!!
        fillEditText(profile)
    }

    private fun fillEditText(profile: Model.Profile?) {
        edit_update_name.setText(profile?.nama)
        edit_update_full_name.setText(profile?.nama_lengkap)
        edit_update_birthday.setText(profile?.tanggal_lahir)
        edit_update_birthday.hint = profile?.tanggal_lahir
        edit_update_address.setText(profile?.alamat)
        edit_update_height.setText(profile?.tinggi_badan.toString())
        edit_update_weight.setText(profile?.berat_badan.toString())
        edit_update_social_media.setText(profile?.social_media)
        edit_update_phone_number.setText(profile?.nomor_telepon)
        setGender(profile)
        setEducation(profile?.pendidikan_terakhir)

        Picasso.with(applicationContext)
            .load(BuildConfig.BASE_URL + profile?.foto)
            .placeholder(R.drawable.placeholder_avatar)
            .noFade()
            .into(update_photo)

        Picasso.with(applicationContext)
            .load(BuildConfig.BASE_URL + profile?.cover)
            .placeholder(R.drawable.placeholder_cover)
            .into(update_cover)

        edit_update_birthday.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                // Display Selected date in Toast
                edit_update_birthday.setText("""$year-${monthOfYear + 1}-$dayOfMonth""")
            }, year, month, day)
            dpd.show()
        }

        update_photo_fab.setOnClickListener {
            if (EasyPermissions.hasPermissions(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)){
                ImagePicker.with(this)
                    .crop(1f,1f)
                    .compress(1260)
                    .maxResultSize(1080,1080)
                    .start(Constant.PHOTO_REQUEST_CODE)

            } else {
                EasyPermissions.requestPermissions(this,"This application need your permission to access photo gallery",
                    991,android.Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
        update_cover_fab.setOnClickListener {
            if (EasyPermissions.hasPermissions(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)){
                ImagePicker.with(this)
                    .crop(1.7f,1f)
                    .compress(4048)
                    .maxResultSize(1080,2860)
                    .start(Constant.COVER_REQUEST_CODE)

            } else {
                EasyPermissions.requestPermissions(this,"This application need your permission to access photo gallery",
                    991,android.Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && data != null){
            if (requestCode == Constant.PHOTO_REQUEST_CODE) {
                update_photo.setImageURI(data.data)
                presenter.uploadPhoto(data.data)
            } else if (requestCode == Constant.COVER_REQUEST_CODE){
                update_cover.setImageURI(data.data)
                presenter.uploadCover(data.data)
            }
        }
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
        profile?.tanggal_lahir = edit_update_birthday.text.toString()
        profile?.alamat = edit_update_address.text.toString()
        profile?.tinggi_badan = edit_update_height.text.toString().toInt()
        profile?.berat_badan = edit_update_weight.text.toString().toInt()
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

    override fun getPhoto(profile: Model.Profile) {
        this.profile.foto = profile.foto
    }

    override fun getCover(profile: Model.Profile) {
        this.profile.cover = profile.cover
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.update_profile_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.update_save){
            val newProfile = constructModel(profile)
            newProfile?.let { presenter.updateProfile(it) }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.stay, R.anim.slide_in_down)
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
            val checked = view.isChecked

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
