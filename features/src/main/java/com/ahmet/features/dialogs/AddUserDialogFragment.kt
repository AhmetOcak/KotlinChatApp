package com.ahmet.features.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.ahmet.core.base.BaseDialogFragment
import com.ahmet.features.databinding.CustomAdduserDialogBinding
import com.ahmet.features.utils.Constants
import com.ahmet.features.utils.FirebaseCommonMessages
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AddUserDialogFragment @Inject constructor() :
    BaseDialogFragment<AddUserDialogViewModel, CustomAdduserDialogBinding>() {

    override fun getViewModelClass(): Class<AddUserDialogViewModel> =
        AddUserDialogViewModel::class.java
    override fun getViewDataBinding(): CustomAdduserDialogBinding =
        CustomAdduserDialogBinding.inflate(layoutInflater)

    @Inject
    lateinit var toast: Toast

    companion object {
        fun newInstance(userEmail: String): AddUserDialogFragment {
            val f = AddUserDialogFragment()
            val args = Bundle()
            args.putString("userEmail", userEmail)
            f.arguments = args
            return f
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        viewModel.userEmail.value = arguments?.getString("userEmail").toString()
        return super.onCreateDialog(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel

        binding.dialogAddUserButtonAUD.setOnClickListener {
            viewModel.addFriend()
            setToastMessage()
            viewModel.errorMessage.value = null
        }
        observeFirebaseResponse()

        binding.dialogCancelButtonAUD.setOnClickListener {
            dismiss()
        }
    }

    override fun setToastMessage() {
        when {
            viewModel.errorMessage.value.toString() == Constants.EMPTY_FIELD_MESSAGE -> {
                toast.setText(Constants.EMPTY_FIELD_MESSAGE)
                toast.show()
            }
            viewModel.errorMessage.value.toString() == Constants.EMAIL_MESSAGE -> {
                toast.setText(Constants.EMAIL_MESSAGE)
                toast.show()
            }
            viewModel.errorMessage.value.toString() == "You can't add yourself" -> {
                toast.setText("You can't add yourself")
                toast.show()
            }
        }
    }

    private fun observeFirebaseResponse() {
        viewModel.firebaseMessage.observe(viewLifecycleOwner) {
            when {
                viewModel.firebaseMessage.value.toString() == FirebaseCommonMessages.NETWORK_ERROR -> {
                    toast.setText(FirebaseCommonMessages.NETWORK_ERROR)
                    toast.show()
                }
                viewModel.firebaseMessage.value.toString() == FirebaseCommonMessages.UNKNOWN_ERROR -> {
                    toast.setText(FirebaseCommonMessages.UNKNOWN_ERROR)
                    toast.show()
                }
                viewModel.firebaseMessage.value.toString() == "You already added this account" -> {
                    toast.setText("You already added this account")
                    toast.show()
                }
                viewModel.firebaseMessage.value.toString() == "There is no such account" -> {
                    toast.setText("There is no such account")
                    toast.show()
                }
                else -> {
                    toast.setText("Friend added successfully")
                    toast.show()
                    dismiss()
                }
            }
        }
    }


}