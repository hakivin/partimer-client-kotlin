package com.szechuanstudio.partimer.data.model

object Model {

    data class LoginResponse(
        val user: User?,
        val access_token: String?
    )

    data class User(val id: Int?,
                    val name: String?,
                    val email: String?,
                    val email_verified_at: String?,
                    val created_at: String?,
                    val updated_at: String?)

    data class ProfileResponse(val profile: List<Profile>)

    data class Profile(val id: Int?,
                       val id_user: Int?,
                       var nama: String?,
                       var nama_lengkap: String?,
                       var nomor_telepon: String?,
                       var jenis_kelamin: String?,
                       var alamat: String?,
                       val email: String?,
                       var social_media: String?,
                       var pendidikan_terakhir: String?,
                       var foto: String?,
                       var cover: String?,
                       var created_at: String?,
                       var updated_at: String?)
}
