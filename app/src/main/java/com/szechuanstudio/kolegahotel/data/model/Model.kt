package com.szechuanstudio.kolegahotel.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

object Model {

    data class LoginObject(val login: LoginResponse?)

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

    data class ProfileResponse(val profile: Profile?)

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
    data class Position(val id: Int?, val nama_posisi: String?, val deskripsi: String?) : Parcelable

    data class JobsAcceptedResponse(val jobs: AcceptedPaginate)

    data class AcceptedPaginate(val current_page: Int?,
                                val data: List<JobAccepted>?,
                                val last_page: Int?)

    data class JobAccepted(val id: Int?,
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
                           val foto: String?,
                           val kuota: Int?,
                           val bayaran: Int?,
                           val deskripsi: String?,
                           val url_slug: String?,
                           val status: String?,
                           val created_at: String?,
                           val updated_at: String?,
                           val dikerjakan_count: Int?,
                           val isApplied: Boolean?,
                           val isExpired: Boolean?,
                           val hotel: Hotel?,
                           val posisi: Position?)

    data class JobsResponse(val jobs: JobPaginate?)

    data class JobPaginate(val current_page: Int?,
                           val data: List<JobData>?,
                           val last_page: Int?)
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
                       val foto: String?,
                       val kuota: Int?,
                       val bayaran: Int?,
                       val deskripsi: String?,
                       val url_slug: String?,
                       val status: String?,
                       val created_at: String?,
                       val updated_at: String?,
                       val dikerjakan_count: Int?,
                       val isApplied: Boolean?,
                       val isExpired: Boolean?,
                       val hotel: Hotel?,
                       val posisi: Position?) : Parcelable {
        constructor() : this(null, null, null, "", "", "",
            "", null, null, null, null,
            "", null, null, "", "", "","","",
            null, null, null, null, null)
    }

    data class JobDetailResponse(val active_jobs: JobDetail?)

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
                         val foto: String?,
                         val kuota: Int?,
                         val bayaran: Int?,
                         val deskripsi: String?,
                         val url_slug: String?,
                         val status: String?,
                         val created_at: String?,
                         val updated_at: String?,
                         val isApplied: Boolean?,
                         val isExpired: Boolean?,
                         val hotel: Hotel?,
                         val posisi: Position?,
                         val todolist: List<ToDoList>)

    data class JobHistoryResponse(val jobs: HistoryPaginate?)

    data class HistoryPaginate(val current_page: Int?,
                               val data: List<JobHistory>?,
                               val last_page: Int?)

    @Parcelize
    data class JobHistory(val id: Int?,
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
                          val foto: String?,
                          val kuota: Int?,
                          val bayaran: Int?,
                          val deskripsi: String?,
                          val url_slug: String?,
                          val status: String?,
                          val created_at: String?,
                          val updated_at: String?,
                          val dikerjakan_count: Int?,
                          val isApplied: Boolean?,
                          val isExpired: Boolean?,
                          val hotel: Hotel?,
                          val posisi: Position?,
                          val dikerjakan: List<PivotJob>?) : Parcelable

    @Parcelize
    data class PivotJob(val id: Int?,
                        val pivot: Pivot?) : Parcelable

    @Parcelize
    data class Pivot(val pekerjaan_id: Int?,
                     val user_id: Int?,
                     val status: String?) : Parcelable

    data class ToDoListResponse(val todolist: List<ToDoList>?)

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
                            val deskripsi: String?,
                            val nomor_telepon: String?,
                            val social_media: String?,
                            val foto: String?,
                            val website: String?,
                            val url_slug: String?) : Parcelable

    data class PageAttrib(var currentPage : Int,
                          var isLastPage : Boolean,
                          var totalPage : Int,
                          var isLoading : Boolean)
}
