package com.unlam.tupartidito.ui.login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.unlam.tupartidito.common.Constants
import com.unlam.tupartidito.common.observe
import com.unlam.tupartidito.common.toast
import com.unlam.tupartidito.databinding.ActivityLoginBinding
import com.unlam.tupartidito.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()
    private lateinit var myPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myPreferences = getSharedPreferences(Constants.MY_PREFERENCES, Context.MODE_PRIVATE)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setObservers()
        setEvents()
        binding.txtEmail.setText("root")
        binding.txtPassword.setText("toor")
    }

    private fun setObservers() {
        with(viewModel) {
            observe(userData) { response ->
                if (response.isValidSession == true) {
                    val intent = Intent(binding.root.context, MainActivity::class.java)
                    intent.putExtra(Constants.MAIN_PARAM, response.user!!.id)
                    startActivity(intent)
                    finish()
                } else {
                    toast(binding.root.context.getString(response.messageError!!))
                }
            }
        }
    }

    private fun setEvents() {
        with(viewModel) {
            binding.btnIngresar.setOnClickListener {
                val email: String = binding.txtEmail.text.toString()
                val password: String = binding.txtPassword.text.toString()

                loginSession(email, password, myPreferences)
            }
            binding.btnRegistro.setOnClickListener {
                val email: String = binding.txtEmail.text.toString()
                val password: String = binding.txtPassword.text.toString()

                registerUser(email, password)
            }
        }
    }

}