package com.szechuanstudio.partimer.ui.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.szechuanstudio.partimer.R
import com.szechuanstudio.partimer.data.model.Model
import com.szechuanstudio.partimer.data.retrofit.RetrofitClient
import com.szechuanstudio.partimer.ui.home.MainActivity
import com.szechuanstudio.partimer.ui.register.RegisterActivity
import com.szechuanstudio.partimer.utils.LoginViewModelFactory
import com.szechuanstudio.partimer.utils.PreferenceUtils
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.singleTop
import org.jetbrains.anko.toast

class LoginActivity : AppCompatActivity(), LoginView {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var presenter : LoginPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        val username = findViewById<EditText>(R.id.username)
        val password = findViewById<EditText>(R.id.password)
        val login = findViewById<Button>(R.id.login)
        val loading = findViewById<ProgressBar>(R.id.loading)
        register_intent.setOnTouchListener { _, _ -> startActivity(intentFor<RegisterActivity>().singleTop())
            true
        }

        presenter = LoginPresenter(this, RetrofitClient.getInstance(), applicationContext)
        init()

        loginViewModel = ViewModelProviders.of(this,
            LoginViewModelFactory()
        )
            .get(LoginViewModel::class.java)

        loginViewModel.loginFormState.observe(this@LoginActivity, Observer {
            val loginState = it ?: return@Observer

            // disable login button unless both username / password is valid
            login.isEnabled = loginState.isDataValid

            if (loginState.usernameError != null) {
                username.error = getString(loginState.usernameError)
            }
            if (loginState.passwordError != null) {
                password.error = getString(loginState.passwordError)
            }
        })

        username.afterTextChanged {
            loginViewModel.loginDataChanged(
                username.text.toString(),
                password.text.toString()
            )
        }

        password.apply {
            afterTextChanged {
                loginViewModel.loginDataChanged(
                    username.text.toString(),
                    password.text.toString()
                )
            }

            login.setOnClickListener {
                loading.visibility = View.VISIBLE
                presenter.login(username.text.toString(), password.text.toString())
            }
        }
    }

    private fun init() {
        loading.visibility = View.VISIBLE
        val email = PreferenceUtils.getEmail(this)
        val password = PreferenceUtils.getPassword(this)
        if (!email.isNullOrEmpty() && !password.isNullOrEmpty())
            presenter.login(email, password)
        else {
            loading.visibility = View.GONE
        }
    }

    private fun updateUiWithUser() {
        startActivity(intentFor<MainActivity>().singleTop())
        finish()
    }

    override fun getUser(user: Model.User?) {
        if (user != null) {
            updateUiWithUser()
        }
        loading.visibility = View.GONE
    }

    override fun failed(message: String?) {
        if (message != null) {
            toast(message)
        }
        loading.visibility = View.GONE
    }

    override fun onResume() {
        super.onResume()
        init()
    }
}

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}
