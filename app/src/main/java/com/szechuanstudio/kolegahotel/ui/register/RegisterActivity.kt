package com.szechuanstudio.kolegahotel.ui.register

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.szechuanstudio.kolegahotel.R
import com.szechuanstudio.kolegahotel.data.model.Model
import com.szechuanstudio.kolegahotel.data.retrofit.RetrofitClient
import com.szechuanstudio.kolegahotel.ui.login.afterTextChanged
import com.szechuanstudio.kolegahotel.ui.main.MainActivity
import kotlinx.android.synthetic.main.activity_register.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.singleTop
import org.jetbrains.anko.toast

class RegisterActivity : AppCompatActivity(), RegisterView {

    private lateinit var registerViewModel : RegisterViewModel
    private lateinit var presenter: RegisterPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        registerViewModel = RegisterViewModel()
        presenter = RegisterPresenter(this, RetrofitClient.getInstance(), applicationContext)

        registerViewModel.registerFormState.observe(this@RegisterActivity, Observer {
            val registerState = it ?: return@Observer

            register.isEnabled = registerState.isDataValid

            if (register.isEnabled)
                register.background = getDrawable(R.drawable.custom_button)

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
            it.isEnabled = false
            presenter.register(name_register.text.toString(),
                email_register.text.toString(),
                password_register.text.toString(),
                password_confirm_register.text.toString())
        }
    }

    override fun registered(user: Model.User?) {
        startActivity(intentFor<MainActivity>().singleTop())
        finish()
    }

    override fun failed(message: String?) {
        message?.let { toast(it) }
        register.isEnabled = true
        loading.visibility = View.GONE
    }
}
