package com.szechuanstudio.kolegahotel.ui.main.ui.profile.update

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import com.szechuanstudio.kolegahotel.R
import com.szechuanstudio.kolegahotel.data.model.Model
import com.szechuanstudio.kolegahotel.data.retrofit.RetrofitClient
import com.szechuanstudio.kolegahotel.utils.Constant
import kotlinx.android.synthetic.main.activity_update_profile.*
import org.jetbrains.anko.toast
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
        if (profile?.tinggi_badan != null)
            edit_update_height.setText(profile.tinggi_badan.toString())
        if (profile?.berat_badan != null)
            edit_update_weight.setText(profile.berat_badan.toString())
        edit_update_social_media.setText(profile?.social_media)
        edit_update_phone_number.setText(profile?.nomor_telepon)
        setGender(profile)
        setEducation(profile?.pendidikan_terakhir)

        edit_update_birthday.setOnClickListener {
            val c = Calendar.getInstance()
            val years = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                // Display Selected date in Toast
                edit_update_birthday.setText("""$year-${monthOfYear + 1}-$dayOfMonth""")
            }, years, month, day)
            dpd.show()
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
        if (edit_update_height.text.toString().isNotEmpty())
            profile?.tinggi_badan = edit_update_height.text.toString().toInt()
        if (edit_update_weight.text.toString().isNotEmpty())
            profile?.berat_badan = edit_update_weight.text.toString().toInt()
        profile?.social_media = edit_update_social_media.text.toString()
        profile?.nomor_telepon = edit_update_phone_number.text.toString()
        profile?.jenis_kelamin = getGender()
        profile?.pendidikan_terakhir = getEducation()
        return profile
    }

    override fun success() {
        finish()
    }

    override fun failed() {
        toast("failed")
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
