package com.szechuanstudio.partimer.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

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

    @Parcelize
    data class Profile(val id: Int?,
                       val user_id: Int?,
                       var nama: String?,
                       var nama_lengkap: String?,
                       var nomor_telepon: String?,
                       var tanggal_lahir: String?,
                       var jenis_kelamin: String?,
                       var tinggi_badan: Int?,
                       var berat_badan: Int?,
                       var alamat: String?,
                       val email: String?,
                       var social_media: String?,
                       var pendidikan_terakhir: String?,
                       var foto: String?,
                       var cover: String?,
                       var created_at: String?,
                       var updated_at: String?) : Parcelable

    data class PositionsResponse(val positions: List<Position>)

    data class Position(val id: Int?, val nama_posisi: String?)

    data class JobsResponse(val jobs: List<Job>)

    data class Job(val id: Int?,
                   val hotel_id: Int?,
                   val posisi_id: Int?,
                   val area: String?,
                   val tanggal_mulai: String?,
                   val waktu_mulai: String?,
                   val waktu_selesai: String?,
                   val tinggi_minimal: Int?,
                   val tinggi_maksimal: Int?,
                   val berat_minimal: Int?,
                   val berat_maksimal: Int?,
                   val kuota: Int?,
                   val bayaran: Int?,
                   val deskripsi: String?,
                   val url_slug: String?,
                   val created_at: String?,
                   val updated_at: String?,
                   val dikerjakan_count: Int?,
                   val hotel: Hotel?,
                   val posisi: Position?)

    data class Hotel(val id: Int?,
                     val profile: HotelProfile?)

    data class HotelProfile(val id: Int?,
                            val hotel_id: Int?,
                            val nama: String?,
                            val alamat: String?,
                            val email: String?,
                            val nomor_telepon: String?,
                            val social_media: String?,
                            val foto: String?,
                            val website: String?,
                            val url_slug: String?)
}
