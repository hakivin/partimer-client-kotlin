package com.szechuanstudio.kolegahotel.ui.main.ui.profile.documents

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.github.dhaval2404.imagepicker.ImagePicker
import com.squareup.picasso.Picasso
import com.szechuanstudio.kolegahotel.BuildConfig
import com.szechuanstudio.kolegahotel.R
import com.szechuanstudio.kolegahotel.data.model.Model
import com.szechuanstudio.kolegahotel.data.retrofit.RetrofitClient
import com.szechuanstudio.kolegahotel.utils.Constant
import kotlinx.android.synthetic.main.activity_document.*
import pub.devrel.easypermissions.EasyPermissions

class DocumentActivity : AppCompatActivity(), DocumentView {

    private lateinit var presenter: DocumentPresenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_document)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Upload Documents"
        showFab(false)
        presenter = DocumentPresenter(this, RetrofitClient.getInstance(), applicationContext)
        presenter.getDocuments()
        initComponent()
    }

    private fun showFab(state : Boolean){
        if (state){
            update_ktp_fab.visibility = View.VISIBLE
            update_skck_fab.visibility = View.VISIBLE
            update_sertif_fab.visibility = View.VISIBLE
            update_kartu_fab.visibility = View.VISIBLE
        } else {
            update_ktp_fab.visibility = View.GONE
            update_skck_fab.visibility = View.GONE
            update_sertif_fab.visibility = View.GONE
            update_kartu_fab.visibility = View.GONE
        }
    }

    private fun initComponent(){
        update_ktp_fab.setOnClickListener {
            if (EasyPermissions.hasPermissions(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)){
                ImagePicker.with(this)
                    .compress(2048)
                    .maxResultSize(1080,1080)
                    .start(Constant.KTP_REQUEST_CODE)
            } else {
                EasyPermissions.requestPermissions(this,"This application need your permission to access photo gallery",
                    991,android.Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
        update_skck_fab.setOnClickListener {
            if (EasyPermissions.hasPermissions(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)){
                ImagePicker.with(this)
                    .compress(2048)
                    .maxResultSize(1080,1080)
                    .start(Constant.SKCK_REQUEST_CODE)
            } else {
                EasyPermissions.requestPermissions(this,"This application need your permission to access photo gallery",
                    991,android.Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
        update_sertif_fab.setOnClickListener {
            if (EasyPermissions.hasPermissions(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)){
                ImagePicker.with(this)
                    .compress(2048)
                    .maxResultSize(1080,1080)
                    .start(Constant.SERTIF_REQUEST_CODE)
            } else {
                EasyPermissions.requestPermissions(this,"This application need your permission to access photo gallery",
                    991,android.Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
        update_kartu_fab.setOnClickListener {
            if (EasyPermissions.hasPermissions(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)){
                ImagePicker.with(this)
                    .compress(2048)
                    .maxResultSize(1080,1080)
                    .start(Constant.KARTU_REQUEST_CODE)
            } else {
                EasyPermissions.requestPermissions(this,"This application need your permission to access photo gallery",
                    991,android.Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && data != null){
            when (requestCode) {
                Constant.KTP_REQUEST_CODE -> {
                    presenter.updateKtp(data.data)
                }
                Constant.SKCK_REQUEST_CODE -> {
                    presenter.updateSkck(data.data)
                }
                Constant.SERTIF_REQUEST_CODE -> {
                    presenter.updateCertificate(data.data)
                }
                Constant.KARTU_REQUEST_CODE -> {
                    presenter.updateCard(data.data)
                }
            }
            shimmer_documents.showShimmer(true)
            showFab(false)
        }
    }

    override fun showDocuments(profile: Model.Profile?) {
        shimmer_documents.hideShimmer()
        showFab(true)

        if (!profile?.ktp.isNullOrBlank()) {
            Picasso.with(applicationContext)
                .load(BuildConfig.BASE_URL + '/' + profile?.ktp)
                .placeholder(R.drawable.placeholder_avatar)
                .noFade()
                .into(ktp_image)
        }

        if (!profile?.skck.isNullOrBlank()) {
            Picasso.with(applicationContext)
                .load(BuildConfig.BASE_URL + '/' + profile?.skck)
                .placeholder(R.drawable.placeholder_avatar)
                .noFade()
                .into(skck_image)
        }

        if (!profile?.sertifikat.isNullOrBlank()) {
            Picasso.with(applicationContext)
                .load(BuildConfig.BASE_URL + '/' + profile?.sertifikat)
                .placeholder(R.drawable.placeholder_avatar)
                .noFade()
                .into(sertif_image)
        }

        if (!profile?.kartu_satpam.isNullOrBlank()) {
            Picasso.with(applicationContext)
                .load(BuildConfig.BASE_URL + '/' + profile?.kartu_satpam)
                .placeholder(R.drawable.placeholder_avatar)
                .noFade()
                .into(kartu_satpam_image)
        }

        if (profile?.status_ktp == 1){
            ktp_status.text = getString(R.string.accepted)
            ktp_status.setTextColor(ContextCompat.getColor(applicationContext, R.color.green_500))
        } else if (profile?.status_ktp == 0){
            ktp_status.text = getString(R.string.rejected)
            ktp_status.setTextColor(ContextCompat.getColor(applicationContext, R.color.red_500))
        }

        if (profile?.status_skck == 1){
            skck_status.text = getString(R.string.accepted)
            skck_status.setTextColor(ContextCompat.getColor(applicationContext, R.color.green_500))
        } else if (profile?.status_skck == 0){
            skck_status.text = getString(R.string.rejected)
            skck_status.setTextColor(ContextCompat.getColor(applicationContext, R.color.red_500))
        }

        if (profile?.status_sertifikat == 1){
            sertif_status.text = getString(R.string.accepted)
            sertif_status.setTextColor(ContextCompat.getColor(applicationContext, R.color.green_500))
        } else if (profile?.status_sertifikat == 0){
            sertif_status.text = getString(R.string.rejected)
            sertif_status.setTextColor(ContextCompat.getColor(applicationContext, R.color.red_500))
        }

        if (profile?.status_kartu_satpam == 1){
            kartu_satpam_status.text = getString(R.string.accepted)
            kartu_satpam_status.setTextColor(ContextCompat.getColor(applicationContext, R.color.green_500))
        } else if (profile?.status_kartu_satpam == 0){
            kartu_satpam_status.text = getString(R.string.rejected)
            kartu_satpam_status.setTextColor(ContextCompat.getColor(applicationContext, R.color.red_500))
        }
    }

    override fun imageUploaded() {
        presenter.getDocuments()
    }
}
