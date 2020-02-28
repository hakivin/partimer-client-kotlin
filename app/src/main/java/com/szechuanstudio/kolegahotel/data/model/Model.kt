package com.szechuanstudio.kolegahotel.data.model

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
                       var ktp: String?,
                       var skck: String?,
                       var sertifikat: String?,
                       var kartu_satpam: String?,
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
                       var status_ktp: Int?,
                       var status_skck: Int?,
                       var status_sertifikat: Int?,
                       var status_kartu_satpam: Int?,
                       var isCompleted: Boolean?,
                       var created_at: String?,
                       var updated_at: String?) : Parcelable

    data class PositionsResponse(val positions: List<Position>)

    @Parcelize
    data class Position(val id: Int?, val nama_posisi: String?) : Parcelable

    data class JobsResponse(val jobs: List<JobData>)

    @Parcelize
    data class JobData(val id: Int?,
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
                       val isApplied: Boolean?,
                       val isExpired: Boolean?,
                       val hotel: Hotel?,
                       val posisi: Position?) : Parcelable

    data class JobDetailResponse(val job_detail: List<JobDetail>)

    data class JobDetail(val id: Int?,
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
                         val isApplied: Boolean?,
                         val isExpired: Boolean?,
                         val todolist: List<ToDoList>)

    data class ToDoList(val id: Int?,
                        val pekerjaan_id: Int?,
                        val nama_pekerjaan: String?)

    @Parcelize
    data class Hotel(val id: Int?,
                     val profile: HotelProfile?) : Parcelable

    @Parcelize
    data class HotelProfile(val id: Int?,
                            val hotel_id: Int?,
                            val nama: String?,
                            val alamat: String?,
                            val email: String?,
                            val nomor_telepon: String?,
                            val social_media: String?,
                            val foto: String?,
                            val website: String?,
                            val url_slug: String?) : Parcelable
}
