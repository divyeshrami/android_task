package com.example.clientdemo.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.clientdemo.MainActivity
import com.example.clientdemo.R
import com.example.clientdemo.databinding.ActivityLoginBinding
import java.util.EnumSet.of
import java.util.Optional.of

class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding
    lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_login
        )

        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        binding.viewModel = loginViewModel

        initialization()
    }

    private fun initialization() {

        initObservables()
    }

    private fun initObservables() {

        loginViewModel.loginLiveData.observe(this, Observer { um ->
            startActivity(Intent(this, MainActivity::class.java).
            putExtra("UM", um.userName))


            finish()
        })

        loginViewModel.nameErorr.observe(this, Observer { s ->
            binding.userNameTextInputLayout.error = s
        })

        loginViewModel.passwordError.observe(this, Observer { s ->
            binding.passwordTextInputLayout.error = s
        })

        loginViewModel.btnSelected.observe(this, Observer { s ->
            binding.loginBtn.isEnabled = s
        })


    }
}
