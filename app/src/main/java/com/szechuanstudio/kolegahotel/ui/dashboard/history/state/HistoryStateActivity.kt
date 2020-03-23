package com.szechuanstudio.kolegahotel.ui.dashboard.history.state

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.szechuanstudio.kolegahotel.R
import com.szechuanstudio.kolegahotel.data.model.Model
import com.szechuanstudio.kolegahotel.data.retrofit.RetrofitClient
import com.szechuanstudio.kolegahotel.ui.main.MainActivity
import com.szechuanstudio.kolegahotel.utils.Constant
import com.szechuanstudio.kolegahotel.utils.PreferenceUtils
import kotlinx.android.synthetic.main.done_state.*
import kotlinx.android.synthetic.main.finish_state.*
import kotlinx.android.synthetic.main.verified_state.*
import okhttp3.ResponseBody
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.singleTop
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HistoryStateActivity : AppCompatActivity() {

    private lateinit var job: Model.JobHistory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history_state)
        job = intent.getParcelableExtra(Constant.KEY_IMAGE_JOB)!!
        when {
            job.dikerjakan?.first()?.pivot?.status.equals("2") -> initDoneState()
            job.dikerjakan?.first()?.pivot?.status.equals("3") -> initVerifiedState()
            job.dikerjakan?.first()?.pivot?.status.equals("4") -> initFinishState()
        }
    }

    private fun initDoneState(){
        setContentView(R.layout.done_state)
        initToolbar()
        if (!job.hotel?.profile?.email.isNullOrBlank()){
            contact_email.setOnClickListener {
                val email = Intent(Intent.ACTION_SEND)
                email.type = "text/email"
                email.putExtra(Intent.EXTRA_EMAIL, arrayOf(job.hotel?.profile?.email))
                startActivity(Intent.createChooser(email, "Send email using:"))
            }
        } else {
            contact_email.visibility = View.GONE
        }

        if (!job.hotel?.profile?.nomor_telepon.isNullOrBlank()){
            contact_phone.setOnClickListener {
                val phone = Intent(Intent.ACTION_DIAL)
                phone.data = Uri.parse("tel:${job.hotel?.profile?.nomor_telepon}")
                startActivity(phone)
            }
        } else {
            contact_phone.visibility = View.GONE
        }
    }

    private fun initVerifiedState(){
        setContentView(R.layout.verified_state)
        initToolbar()
        if (!job.hotel?.profile?.email.isNullOrBlank()){
            ver_contact_email.setOnClickListener {
                val email = Intent(Intent.ACTION_SEND)
                email.type = "text/email"
                email.putExtra(Intent.EXTRA_EMAIL, arrayOf(job.hotel?.profile?.email))
                startActivity(Intent.createChooser(email, "Send email using:"))
            }
        } else {
            ver_contact_email.visibility = View.GONE
        }

        if (!job.hotel?.profile?.nomor_telepon.isNullOrBlank()){
            ver_contact_phone.setOnClickListener {
                val phone = Intent(Intent.ACTION_DIAL)
                phone.data = Uri.parse("tel:${job.hotel?.profile?.nomor_telepon}")
                startActivity(phone)
            }
        } else {
            ver_contact_phone.visibility = View.GONE
        }

        done_button.setOnClickListener {
            it.isEnabled = false
            AlertDialog.Builder(this)
                .setTitle("Warning")
                .setMessage("Do you have already received payment? You can not cancel it later")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes
                ) { _, _ ->
                    RetrofitClient.getInstance().jobPaid(job.id, PreferenceUtils.getToken(applicationContext))
                        .enqueue(object : Callback<ResponseBody>{
                            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                                toast("Something went wrong. Try again later")
                                it.isEnabled = true
                            }

                            override fun onResponse(
                                call: Call<ResponseBody>,
                                response: Response<ResponseBody>
                            ) {
                                if (response.isSuccessful){
                                    finish()
                                    toast("Congratulation! You have finished your job")
                                } else {
                                    println(response.code())
                                    toast("Something went wrong. Try again later")
                                }
                                it.isEnabled = true
                            }

                        })
                }
                .setNegativeButton(android.R.string.no, null).show()
        }
    }

    private fun initFinishState(){
        setContentView(R.layout.finish_state)
        initToolbar()
        find_button.setOnClickListener {
            navigateUpTo(intentFor<MainActivity>().singleTop())
        }
    }

    private fun initToolbar(){
        supportActionBar?.title = job.hotel?.profile?.nama
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}
