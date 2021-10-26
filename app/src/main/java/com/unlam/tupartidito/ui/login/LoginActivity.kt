package com.unlam.tupartidito.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.unlam.tupartidito.common.Constants
import com.unlam.tupartidito.common.observe
import com.unlam.tupartidito.common.toast
import com.unlam.tupartidito.databinding.ActivityLoginBinding
import com.unlam.tupartidito.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLoginBinding
    private val viewModel : LoginViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setObservers()
        setEvents()
        binding.txtEmail.setText("root")
        binding.txtPassword.setText("toor")
    }

    private fun setObservers(){
        with(viewModel){
            observe(userData){ response ->
                if(response.isValidSession == true) {
                    val intent = Intent(binding.root.context, MainActivity::class.java)
                    intent.putExtra(Constants.MAIN_PARAM,response.user!!.id)
                    startActivity(intent)
                } else {
                    toast(response.messageError.toString())
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