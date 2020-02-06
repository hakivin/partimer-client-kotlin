package com.szechuanstudio.partimer.ui.main.ui.profile

import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.squareup.picasso.Picasso
import com.szechuanstudio.partimer.BuildConfig
import com.szechuanstudio.partimer.R
import com.szechuanstudio.partimer.data.model.Model
import com.szechuanstudio.partimer.data.retrofit.RetrofitClient
import com.szechuanstudio.partimer.ui.login.LoginActivity
import com.szechuanstudio.partimer.ui.main.ui.profile.positions.UpdatePositionActivity
import com.szechuanstudio.partimer.ui.main.ui.profile.update.UpdateProfileActivity
import com.szechuanstudio.partimer.utils.Constant
import com.szechuanstudio.partimer.utils.PreferenceUtils
import kotlinx.android.synthetic.main.fragment_profile.*
import org.jetbrains.anko.singleTop
import org.jetbrains.anko.support.v4.act
import org.jetbrains.anko.support.v4.ctx
import org.jetbrains.anko.support.v4.intentFor
import org.jetbrains.anko.support.v4.toast
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ProfileFragment : Fragment(), ProfileView {

    private lateinit var presenter: ProfilePresenter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = ProfilePresenter(this, RetrofitClient.getInstance(), act.applicationContext)
        presenter.checkProfile()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.profile_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.logout){
            presenter.logout()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setGender(string: String?) : String?{
        if (string.equals("L"))
            return "Male"
        else if(string.equals("P"))
            return "Female"
        return null
    }

    private fun setEducation(string: String?) : String?{
        return when {
            string.equals("SD") -> "Elementary School"
            string.equals("SMP") -> "Middle School"
            string.equals("SMA") -> "High School"
            string.equals("Diploma") -> "Diploma"
            string.equals("S1") -> "Bachelor"
            string.equals("S2") -> "Master"
            string.equals("S3") -> "Doctorate"
            else -> null
        }
    }

    private fun convertDate(string: String?) : String? {
        val date = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && !string.isNullOrEmpty()) {
            LocalDate.parse(string, DateTimeFormatter.ISO_DATE)
        } else {
            return string
        }

        return "${date.dayOfMonth} ${date.month.name.toLowerCase().capitalize()} ${date.year}"
    }

    override fun onResume() {
        super.onResume()
        presenter.checkProfile()
    }

    override fun showProfile(profile: Model.Profile?) {
        if (isAdded) {
            profile_full_name.text = profile?.nama_lengkap
            profile_birthday.text = convertDate(profile?.tanggal_lahir)
            profile_gender.text = setGender(profile?.jenis_kelamin)
            profile_height_weight.text = setHeightWeight(profile?.tinggi_badan, profile?.berat_badan)
            profile_education.text = setEducation(profile?.pendidikan_terakhir)
            profile_phone.text = profile?.nomor_telepon
            profile_email.text = profile?.email
            profile_address.text = profile?.alamat
            profile_social_media.text = profile?.social_media
            Picasso.with(activity?.applicationContext)
                .load(BuildConfig.BASE_URL + profile?.foto)
                .placeholder(R.drawable.placeholder_avatar)
                .noFade()
                .into(profile_photo)

            Picasso.with(activity?.applicationContext)
                .load(BuildConfig.BASE_URL + profile?.cover)
                .placeholder(R.drawable.placeholder_cover)
                .into(profile_cover)

            btn_edit_profile.setOnClickListener {
                startActivity(intentFor<UpdateProfileActivity>(Constant.KEY_PROFILE to profile).singleTop())
                act.overridePendingTransition(R.anim.slide_in_up, R.anim.stay)
            }

            btn_edit_position.setOnClickListener {
                startActivity(intentFor<UpdatePositionActivity>(Constant.KEY_PROFILE to profile).singleTop())
                act.overridePendingTransition(R.anim.slide_in_up, R.anim.stay)
            }
        }
    }

    private fun setHeightWeight(height: Int?, weight: Int?): CharSequence? {
        val h: CharSequence? = if (height != null)
            "$height cm"
        else
            ""
        val w: CharSequence? = if (weight != null)
            "$weight kg"
        else
            ""
        return "$h / $w"
    }

    override fun reject(message: String?) {
        if (isAdded)
            message?.let { toast(it) }
    }

    override fun logoutSuccess() {
        PreferenceUtils.reset(act.applicationContext)
        startActivity(intentFor<LoginActivity>().singleTop())
        act.finish()
    }

    override fun logoutFailed() {
        if (isAdded)
            toast("Logout Failed")
    }

    override fun showPositions(positions: Model.PositionsResponse?) {
        if (rv_positions != null){
            rv_positions.layoutManager = LinearLayoutManager(ctx, LinearLayoutManager.HORIZONTAL, false)
            rv_positions.adapter = positions?.positions?.let { PositionAdapter(it) }
        }
    }
}
