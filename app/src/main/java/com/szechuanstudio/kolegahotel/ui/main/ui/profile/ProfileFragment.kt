package com.szechuanstudio.kolegahotel.ui.main.ui.profile

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.squareup.picasso.Picasso
import com.szechuanstudio.kolegahotel.BuildConfig
import com.szechuanstudio.kolegahotel.R
import com.szechuanstudio.kolegahotel.data.model.Model
import com.szechuanstudio.kolegahotel.data.retrofit.RetrofitClient
import com.szechuanstudio.kolegahotel.ui.login.LoginActivity
import com.szechuanstudio.kolegahotel.ui.main.ui.profile.documents.DocumentActivity
import com.szechuanstudio.kolegahotel.ui.main.ui.profile.positions.UpdatePositionActivity
import com.szechuanstudio.kolegahotel.ui.main.ui.profile.update.UpdateProfileActivity
import com.szechuanstudio.kolegahotel.utils.BaseFragment
import com.szechuanstudio.kolegahotel.utils.Constant
import com.szechuanstudio.kolegahotel.utils.PreferenceUtils
import com.szechuanstudio.kolegahotel.utils.Utils
import kotlinx.android.synthetic.main.fragment_profile.*
import org.jetbrains.anko.singleTop
import org.jetbrains.anko.support.v4.act
import org.jetbrains.anko.support.v4.ctx
import org.jetbrains.anko.support.v4.intentFor
import org.jetbrains.anko.support.v4.toast

class ProfileFragment : BaseFragment(), ProfileView, SwipeRefreshLayout.OnRefreshListener {

    private lateinit var presenter: ProfilePresenter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return getPersistentView(inflater, container, savedInstanceState, R.layout.fragment_profile)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!hasInitializedRootView){
            presenter = ProfilePresenter(this, RetrofitClient.getInstance(), act.applicationContext)
            presenter.checkProfile()
            refresh_profile.setOnRefreshListener(this)
        }
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

//    override fun onResume() {
//        super.onResume()
//        shimmer_android.showShimmer(true)
//        presenter.checkProfile()
//    }

    override fun showProfile(profile: Model.Profile?) {
        if (isAdded) {
            shimmer_android.hideShimmer()
            refresh_profile.isRefreshing = false
            if (profile != null) {
                profile_full_name.text = profile.nama_lengkap
                profile_birthday.text = Utils.convertDate(profile.tanggal_lahir)
                profile_gender.text = setGender(profile.jenis_kelamin)
                profile_height_weight.text =
                    setHeightWeight(profile.tinggi_badan, profile.berat_badan)
                profile_education.text = setEducation(profile.pendidikan_terakhir)
                profile_phone.text = profile.nomor_telepon
                profile_email.text = profile.email
                profile_address.text = profile.alamat
                profile_social_media.text = profile.social_media
                profile_documents.text = getDocumentStatus(profile)
                Log.d(this.tag, "showProfile: ${profile.isCompleted}")
                Picasso.with(act.applicationContext)
                    .load(BuildConfig.BASE_URL + '/' + profile.foto)
                    .placeholder(R.drawable.placeholder_avatar)
                    .noFade()
                    .into(profile_photo)

                Picasso.with(act.applicationContext)
                    .load(BuildConfig.BASE_URL + '/' + profile.cover)
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

                btn_edit_documents.setOnClickListener {
                    startActivity(intentFor<DocumentActivity>(Constant.KEY_DOCUMENTS to profile).singleTop())
                    act.overridePendingTransition(R.anim.slide_in_up, R.anim.stay)
                }

                if(profile.isCompleted!!)
                    verified_profile.visibility = View.VISIBLE
            }
        }
    }

    private fun getDocumentStatus(profile: Model.Profile) : CharSequence{
        return if(profile.ktp.isNullOrBlank() || profile.skck.isNullOrBlank()) "Please Upload Your Documents"
        else if (profile.status_ktp == null || profile.status_skck == null) "Waiting Verification"
        else if (profile.status_ktp == 0 || profile.status_skck == 0 || profile.status_sertifikat == 0 || profile.status_kartu_satpam == 0) "Documents Rejected"
        else if (profile.isCompleted == true) "Completed"
        else "Waiting Verification"
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
        if (isAdded) {
            message?.let { toast(it) }
            shimmer_android.hideShimmer()
        }
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
        if (isAdded){
            rv_positions.layoutManager = LinearLayoutManager(ctx, LinearLayoutManager.HORIZONTAL, false)
            rv_positions.adapter = positions?.positions?.let { PositionAdapter(it) }
        }
    }

    override fun onRefresh() {
        presenter.checkProfile()
    }
}
