package com.szechuanstudio.partimer.ui.register

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.szechuanstudio.partimer.R
import com.szechuanstudio.partimer.data.model.Model
import com.szechuanstudio.partimer.data.retrofit.RetrofitClient
import com.szechuanstudio.partimer.ui.login.afterTextChanged
import kotlinx.android.synthetic.main.activity_register.*
import org.jetbrains.anko.toast

class RegisterActivity : AppCompatActivity(), RegisterView {

    private lateinit var registerViewModel : RegisterViewModel
    private lateinit var presenter: RegisterPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        registerViewModel = RegisterViewModel()
        presenter = RegisterPresenter(this, RetrofitClient.create(), applicationContext)

        registerViewModel.registerFormState.observe(this@RegisterActivity, Observer {
            val registerState = it ?: return@Observer

            register.isEnabled = registerState.isDataValid

            if (registerState.nameError != null){
                name_register.error = getString(registerState.nameError)
            }
            if (registerState.emailError != null){
                email_register.error = getString(registerState.emailError)
            }
            if (registerState.passwordError != null){
                password_register.error = getString(registerState.passwordError)
            }
            if (registerState.confirmedError != null){
                password_confirm_register.error = getString(registerState.confirmedError)
            }
        })

        name_register.afterTextChanged {
            registerViewModel.registerDataChanged(
                name_register.text.toString(),
                email_register.text.toString(),
                password_register.text.toString(),
                password_confirm_register.text.toString())
        }

        email_register.afterTextChanged {
            registerViewModel.registerDataChanged(
                name_register.text.toString(),
                email_register.text.toString(),
                password_register.text.toString(),
                password_confirm_register.text.toString())
        }

        password_register.apply {
            afterTextChanged {
                registerViewModel.registerDataChanged(
                    name_register.text.toString(),
                    email_register.text.toString(),
                    password_register.text.toString(),
                    password_confirm_register.text.toString())
            }
        }

        password_confirm_register.apply {
            afterTextChanged {
                registerViewModel.registerDataChanged(
                    name_register.text.toString(),
                    email_register.text.toString(),
                    password_register.text.toString(),
                    password_confirm_register.text.toString())
            }
        }

        register.setOnClickListener {
            loading.visibility = View.VISIBLE
            presenter.register(name_register.text.toString(),
                email_register.text.toString(),
                password_register.text.toString(),
                password_confirm_register.text.toString())
        }
    }

    override fun registered(user: Model.User?) {
        finish()
    }

    override fun failed(message: String?) {
        message?.let { toast(it) }
        loading.visibility = View.GONE
    }
}
