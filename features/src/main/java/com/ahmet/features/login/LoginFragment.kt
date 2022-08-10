package com.ahmet.features.login

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.ahmet.core.base.BaseFragment
import com.ahmet.features.R
import com.ahmet.features.databinding.FragmentLoginBinding
import com.ahmet.features.utils.*
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : BaseFragment<LoginViewModel, FragmentLoginBinding>() {

    override fun getViewModelClass() = LoginViewModel::class.java
    override fun getViewDataBinding() = FragmentLoginBinding.inflate(layoutInflater)

    @Inject
    lateinit var toast: Toast

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel

        // if user cached, then we will directly going to message fragment
        if (viewModel.isUserCached() && !requireArguments().getBoolean(Constants.IS_COME_FROM_APP)) {
            goToNextScreen(R.id.action_loginFragment_to_messagesFragment, null, null)
        }

        binding.signUp.setOnClickListener {
            goToNextScreen(R.id.action_loginFragment_to_registerFragment, null, null)
        }

        binding.loginButton.setOnClickListener {
            viewModel.login()
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
            viewModel.message.value.toString() == Constants.PASSWORD_LENGTH_MESSAGE -> {
                toast.setText(Constants.PASSWORD_LENGTH_MESSAGE)
                toast.show()
            }
        }
    }

    private fun observeFirebaseResponse() {
        viewModel.firebaseMessage.observe(viewLifecycleOwner) {
            when {
                viewModel.firebaseMessage.value.toString() == FirebaseLoginMessages.ERROR_MESSAGE -> {
                    toast.setText(FirebaseLoginMessages.LOGIN_ERROR)
                    toast.show()
                }
                viewModel.firebaseMessage.value.toString() == FirebaseLoginMessages.LOGIN_PASSWORD_ERROR -> {
                    toast.setText(FirebaseLoginMessages.LOGIN_PASSWORD_ERROR)
                    toast.show()
                }
                viewModel.firebaseMessage.value.toString() == FirebaseCommonMessages.NETWORK_ERROR -> {
                    toast.setText(FirebaseCommonMessages.NETWORK_ERROR)
                    toast.show()
                }
                viewModel.firebaseMessage.value == FirebaseLoginMessages.BLOCKED_ERROR_MESSAGE -> {
                    toast.setText(FirebaseLoginMessages.BLOCKED_ERROR_MESSAGE.substring(0, FirebaseLoginMessages.BLOCKED_ERROR_MESSAGE.indexOf('[')))
                    toast.show()
                }
                viewModel.firebaseMessage.value == FirebaseLoginMessages.SUCCESSFUL -> {
                    toast.setText(FirebaseLoginMessages.SUCCESSFUL)
                    toast.show()

                    observeResponseStatus()
                }
                else -> {
                    toast.setText(FirebaseCommonMessages.UNKNOWN_ERROR)
                    toast.show()
                }
            }
        }
    }

    private fun observeResponseStatus() {
        viewModel.status.observe(viewLifecycleOwner) {
            when (viewModel.status.value) {
                Status.DONE -> {
                    if (viewModel.rememberMeCheckBox.value == false) {
                        goToNextScreen(
                            R.id.action_loginFragment_to_messagesFragment,
                            viewModel.email.value.toString(),
                            Constants.USER_EMAIL_ARG_NAME
                        )
                    } else {
                        goToNextScreen(
                            R.id.action_loginFragment_to_messagesFragment,
                            null,
                            null
                        )
                    }
                }
                else -> {}
            }
        }
    }

}