package com.ahmet.features.login

import android.os.Bundle
import android.view.View
import com.ahmet.core.base.BaseFragment
import com.ahmet.features.R
import com.ahmet.features.databinding.FragmentLoginBinding

class LoginFragment : BaseFragment<LoginViewModel, FragmentLoginBinding>() {

    override fun getViewModelClass() = LoginViewModel::class.java
    override fun getViewDataBinding() = FragmentLoginBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.signUp.setOnClickListener {
            goToNextScreen(R.id.action_loginFragment_to_registerFragment)
        }
        binding.loginButton.setOnClickListener {
            goToNextScreen(R.id.action_loginFragment_to_messagesFragment)
        }
    }
}