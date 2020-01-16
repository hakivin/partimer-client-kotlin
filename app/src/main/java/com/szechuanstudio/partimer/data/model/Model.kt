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
}
