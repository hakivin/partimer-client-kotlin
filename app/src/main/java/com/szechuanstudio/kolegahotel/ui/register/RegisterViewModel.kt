package com.szechuanstudio.kolegahotel.ui.register

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.szechuanstudio.kolegahotel.R

class RegisterViewModel : ViewModel() {

    private val _registerForm = MutableLiveData<RegisterFormState>()
    val registerFormState: LiveData<RegisterFormState> = _registerForm

    fun registerDataChanged(name: String, email: String, password: String, confPassword: String) {
        if (!isUserNameValid(name)) {
            _registerForm.value = RegisterFormState(nameError = R.string.invalid_username)
        } else if(!isEmailValid(email)){
            _registerForm.value = RegisterFormState(emailError = R.string.invalid_email)
        } else if (!isPasswordValid(password)) {
            _registerForm.value = RegisterFormState(passwordError = R.string.invalid_password)
        } else if(!isConfirmed(password, confPassword)) {
            _registerForm.value = RegisterFormState(confirmedError = R.string.invalid_password_confirmation)
        } else {
            _registerForm.value = RegisterFormState(isDataValid = true)
        }
    }

    // A placeholder username validation check
    private fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isConfirmed(password: String, confPassword: String) : Boolean {
        return password == confPassword
    }

    private fun isUserNameValid(username: String) : Boolean {
        return username.length >= 4
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length >= 8
    }
}