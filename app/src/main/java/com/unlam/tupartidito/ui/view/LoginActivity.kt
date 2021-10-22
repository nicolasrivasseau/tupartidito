package com.unlam.tupartidito.ui.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import androidx.activity.viewModels
import com.google.android.material.snackbar.Snackbar
import com.unlam.tupartidito.core.observe
import com.unlam.tupartidito.databinding.ActivityLoginBinding
import com.unlam.tupartidito.ui.viewmodel.LoginViewModel
import com.unlam.tupartidito.ui.viewmodel.MainViewModel

class LoginActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLoginBinding
    private val viewModel : LoginViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setObservers()
        setEvents()
    }

    private fun setObservers(){
        with(viewModel){
            observe(userData){ response ->
                if(response.isValidSession == true) {
                    val intent = Intent(binding.root.context, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    Snackbar.make(binding.root, response.messageError.toString(), Snackbar.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private fun setEvents(){
        with(viewModel){
            binding.btnIngresar.setOnClickListener {
                val email : String = binding.txtEmail.text.toString()
                val password : String = binding.txtPassword.text.toString()
                loginSession(email,password)
            }
        }
    }

}