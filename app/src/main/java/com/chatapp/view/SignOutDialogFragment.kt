package com.chatapp.view

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.chatapp.R

class SignOutDialogFragment(
    private val message: String,
    private val positiveButtonText: String,
    private val negativeButtonText: String
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage(message)
                .setPositiveButton(
                    positiveButtonText
                ) { _, _ ->
                    if(message == getString(R.string.delete_account_message)){
                        // delete account
                    }else {
                        goToLoginScreen()
                    }
                }
                .setNegativeButton(
                    negativeButtonText
                ) { _, _ -> }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun goToLoginScreen() {
        findNavController().navigate(R.id.action_accountSettingsFragment_to_loginFragment)
    }

}
