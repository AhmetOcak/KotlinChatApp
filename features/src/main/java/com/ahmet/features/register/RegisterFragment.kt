package com.ahmet.features.register

import android.os.Bundle
import android.view.View
import com.ahmet.core.base.BaseFragment
import com.ahmet.features.R
import com.ahmet.features.databinding.FragmentRegisterBinding

class RegisterFragment : BaseFragment<RegisterViewModel, FragmentRegisterBinding>() {

    override fun getViewModelClass() = RegisterViewModel::class.java
    override fun getViewDataBinding() = FragmentRegisterBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.registerButton.setOnClickListener {
            goToNextScreen(R.id.action_registerFragment_to_loginFragment)
        }
    }
}