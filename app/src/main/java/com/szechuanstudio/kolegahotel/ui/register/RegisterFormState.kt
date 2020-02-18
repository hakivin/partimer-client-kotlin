package com.szechuanstudio.kolegahotel.ui.register

data class RegisterFormState(
    val nameError: Int? = null,
    val emailError: Int? = null,
    val passwordError: Int? = null,
    val confirmedError: Int? = null,
    val isDataValid : Boolean = false
)