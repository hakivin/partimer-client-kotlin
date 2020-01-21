package com.szechuanstudio.partimer.ui.main.ui.profile


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
import com.szechuanstudio.partimer.R
import com.szechuanstudio.partimer.data.model.Model
import com.szechuanstudio.partimer.data.retrofit.RetrofitClient
import com.szechuanstudio.partimer.ui.main.ui.profile.update.UpdateProfileActivity
import com.szechuanstudio.partimer.utils.Constant
import com.szechuanstudio.partimer.utils.PreferenceUtils
import kotlinx.android.synthetic.main.fragment_profile.*
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.singleTop
import org.jetbrains.anko.support.v4.intentFor
import org.jetbrains.anko.support.v4.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : Fragment() {

    private lateinit var profile: Model.Profile
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkProfile()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_edit_profile.setOnClickListener {
            startActivity(intentFor<UpdateProfileActivity>(Constant.KEY_PROFILE to profile).singleTop())
        }
    }

    private fun showProfile(profile : Model.Profile?){
        profile_full_name.text = profile?.nama_lengkap
        profile_gender.text = profile?.jenis_kelamin
        profile_education.text = profile?.pendidikan_terakhir
        profile_phone.text = profile?.nomor_telepon
        profile_email.text = profile?.email
        profile_address.text = profile?.alamat
        profile_social_media.text = profile?.social_media
        Picasso.with(activity?.applicationContext).load(R.drawable.placeholder_avatar).noFade().into(profile_photo)
        profile_cover.backgroundColor = R.color.colorPrimary
    }

    private fun checkProfile(){
        RetrofitClient.getInstance().getProfile(PreferenceUtils.getId(activity?.applicationContext!!))
            .enqueue(object : Callback<Model.ProfileResponse> {
                override fun onFailure(call: Call<Model.ProfileResponse>, t: Throwable) {
                    toast("Fatal Error\n${t.message}")
                    activity?.finish()
                }

                override fun onResponse(
                    call: Call<Model.ProfileResponse>,
                    response: Response<Model.ProfileResponse>
                ) {
                    val profile = response.body()?.profile?.get(0)
                    if (profile == null)
                        toast("Something went wrong").also { activity?.finish() }
                    else {
                        this@ProfileFragment.profile = profile
                        showProfile(profile)
                        if (profile.nomor_telepon.isNullOrEmpty() || profile.alamat.isNullOrEmpty()) {
                            startActivity(intentFor<UpdateProfileActivity>(Constant.KEY_PROFILE to profile).singleTop())
                            toast("Please complete your identities first")
                        }
                    }
                }
            })
    }
}
