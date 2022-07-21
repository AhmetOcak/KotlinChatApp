package com.ahmet.features.dialogs

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.ahmet.features.databinding.CustomAdduserDialogBinding
import com.ahmet.features.utils.Constants
import com.ahmet.features.utils.FirebaseCommonMessages
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AddUserDialogFragment @Inject constructor(): DialogFragment() {

    private lateinit var binding: CustomAdduserDialogBinding
    private val viewmodel : AddUserDialogViewModel by viewModels()

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
        viewmodel.userEmail.value = arguments?.getString("userEmail").toString()
        return super.onCreateDialog(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding = CustomAdduserDialogBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewmodel

        binding.dialogAddUserButtonAUD.setOnClickListener {
            viewmodel.addFriend()
            setToastMessage()
            viewmodel.errorMessage.value = null
        }
        observeFirebaseResponse()

        binding.dialogCancelButtonAUD.setOnClickListener {
            dismiss()
        }
    }

    private fun setToastMessage() {
        when {
            viewmodel.errorMessage.value.toString() == Constants.EMPTY_FIELD_MESSAGE -> {
                toast.setText(Constants.EMPTY_FIELD_MESSAGE)
                toast.show()
            }
            viewmodel.errorMessage.value.toString() == Constants.EMAIL_MESSAGE -> {
                toast.setText(Constants.EMAIL_MESSAGE)
                toast.show()
            }
            viewmodel.errorMessage.value.toString() == "You can't add yourself" -> {
                toast.setText("You can't add yourself")
                toast.show()
            }
        }
    }

    private fun observeFirebaseResponse() {
        viewmodel.firebaseMessage.observe(viewLifecycleOwner) {
            when {
                viewmodel.firebaseMessage.value.toString() == FirebaseCommonMessages.NETWORK_ERROR -> {
                    toast.setText(FirebaseCommonMessages.NETWORK_ERROR)
                    toast.show()
                }
                viewmodel.firebaseMessage.value.toString() == FirebaseCommonMessages.UNKNOWN_ERROR -> {
                    toast.setText(FirebaseCommonMessages.UNKNOWN_ERROR)
                    toast.show()
                }
                viewmodel.firebaseMessage.value.toString() == "You already added this account" -> {
                    toast.setText("You already added this account")
                    toast.show()
                }
                viewmodel.firebaseMessage.value.toString() == "There is no such account" -> {
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