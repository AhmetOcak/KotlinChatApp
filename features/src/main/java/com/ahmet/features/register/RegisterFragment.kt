package com.ahmet.features.register

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Toast
import com.ahmet.core.base.BaseFragment
import com.ahmet.features.R
import com.ahmet.features.databinding.FragmentRegisterBinding
import com.ahmet.features.utils.*

class RegisterFragment : BaseFragment<RegisterViewModel, FragmentRegisterBinding>() {

    override fun getViewModelClass() = RegisterViewModel::class.java
    override fun getViewDataBinding() = FragmentRegisterBinding.inflate(layoutInflater)
    private lateinit var toast: Toast

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel

        // Toast definition
        toast = Toast.makeText(requireContext(), " ", Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.TOP, 0, 0)

        binding.registerButton.setOnClickListener {
            viewModel.register()
            setToastMessage()
            viewModel.message.value = null
        }
        observeFirebaseResponse()
    }

    private fun setToastMessage() {
        when {
            viewModel.message.value.toString() == Constants.EMPTY_FIELD_MESSAGE -> {
                toast.setText(Constants.EMPTY_FIELD_MESSAGE)
                toast.show()
            }
            viewModel.message.value.toString() == Constants.EMAIL_MESSAGE -> {
                toast.setText(Constants.EMAIL_MESSAGE)
                toast.show()
            }
            viewModel.message.value.toString() == Constants.PASSWORD_MATCH_MESSAGE -> {
                toast.setText(Constants.PASSWORD_MATCH_MESSAGE)
                toast.show()
            }
            viewModel.message.value.toString() == Constants.PASSWORD_LENGTH_MESSAGE -> {
                toast.setText(Constants.PASSWORD_LENGTH_MESSAGE)
                toast.show()
            }
        }
    }

    private fun observeFirebaseResponse() {
        viewModel.firebaseMessage.observe(viewLifecycleOwner) {
            when {
                viewModel.firebaseMessage.value.toString() == FirebaseRegisterMessages.EMAIL_ERROR -> {
                    toast.setText(FirebaseRegisterMessages.EMAIL_ERROR)
                    toast.show()
                }
                viewModel.firebaseMessage.value.toString() == FirebaseCommonMessages.NETWORK_ERROR -> {
                    toast.setText(FirebaseCommonMessages.NETWORK_ERROR)
                    toast.show()
                }
                else -> {
                    toast.setText(FirebaseRegisterMessages.SUCCESSFUL)
                    toast.show()
                    goToNextScreen(R.id.action_registerFragment_to_loginFragment)
                }
            }
        }
    }
}





